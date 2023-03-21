#include "nuclei.h"
token_l* initLexicalAnalysis(char* fileName)
{
  FILE* fp=fopen(fileName,"r");
  if(fp==NULL){
    printf("open file error!\n");
    exit(1);
  }

  char keywords[KEYWORDSNUM][KEYWORDLEN];
  initKeyWord(keywords);
  token_l* tokenList=calloc(1,sizeof(token_l));
  token_l* tokenListTemp=tokenList;
  tokenListTemp->cur=calloc(1,sizeof(token_n));
  while(ScanOneToken(&(tokenListTemp->cur),fp,keywords)!=N_END){
    tokenListTemp->next=calloc(1,sizeof(token_l));
    tokenListTemp=tokenListTemp->next;
    tokenListTemp->cur=calloc(1,sizeof(token_n));
  }
  fclose(fp);
  return tokenList;
}

int ScanOneToken(token_n** token,FILE* fp,char keywords[][KEYWORDLEN])
{
  int ch=getc(fp);
  while(isspace(ch)){
    ch=getc(fp);
  }
  while(ch=='#'){
    while(getc(fp)!='\n');
    ch=getc(fp);
    while(isspace(ch)){
      ch=getc(fp);
    }
  }
  if(ch=='('){
    (*token)->type=N_LPAREN;
  }else if(ch==')'){
    (*token)->type=N_RPAREN;
  }else if(ch>='A' && ch<='Z'){
    checkK_V(token,fp,ch,keywords);
  }else if(ch=='\''){
    checkLiteral(token,fp,ch);
  }else if(ch=='\"'){
    checkString(token,fp,ch);
  }else if(ch==EOF){
    (*token)->type=N_END;
  }else{
    (*token)->type=N_UNKNOWN;
  }
  return (*token)->type;
}

void initKeyWord(char keywords[][KEYWORDLEN])
{
  strcpy(keywords[0],"CAR");
  strcpy(keywords[1],"CDR");
  strcpy(keywords[2],"CONS");
  strcpy(keywords[3],"PLUS");
  strcpy(keywords[4],"LENGTH");
  strcpy(keywords[5],"LESS");
  strcpy(keywords[6],"EQUAL");
  strcpy(keywords[7],"GREATER");
  strcpy(keywords[8],"SET");
  strcpy(keywords[9],"PRINT");
  strcpy(keywords[10],"IF");
  strcpy(keywords[11],"WHILE");
  strcpy(keywords[12],"NIL");
  #ifdef EXTENSION
  strcpy(keywords[13],"MINUS");
  strcpy(keywords[14],"MULTIPLY");
  strcpy(keywords[15],"DIVISION");
  #endif
}

void checkK_V(token_n** token,FILE* fp,char ch,char keywords[][KEYWORDLEN])
{
  int index0=0;
  (*token)->stringValue[index0++]=ch;
  while(isupper(ch=getc(fp))){
    (*token)->stringValue[index0++]=ch;
  }
  (*token)->stringValue[index0++]='\0';
  ungetc(ch,fp);
  if(isKeyWord((*token)->stringValue,keywords)){
    (*token)->type=isKeyWord((*token)->stringValue,keywords);
  }else{
    (*token)->type=N_VAR;
  }
}

void checkLiteral(token_n** token,FILE* fp,char ch)
{
  int index0=0;
  (*token)->stringValue[index0++]=ch;
  while((ch=getc(fp))!='\''){
    if(ch==EOF){
      (*token)->type=N_UNKNOWN;
      return;
    }
    (*token)->stringValue[index0++]=ch;
  }
  (*token)->stringValue[index0++]=ch;
  (*token)->stringValue[index0++]='\0';
  (*token)->type=N_LITERAL;
}

void checkString(token_n** token,FILE* fp,char ch)
{
  int index0=0;
  (*token)->stringValue[index0++]=ch;
  while((ch=getc(fp))!='\"'){
    if(ch==EOF){
      (*token)->type=N_UNKNOWN;
      return;
    }
    (*token)->stringValue[index0++]=ch;
  }
  (*token)->stringValue[index0++]=ch;
  (*token)->stringValue[index0++]='\0';
  (*token)->type=N_STRING;
}


int isKeyWord(char stringValue[],char keywords[][KEYWORDLEN])
{
  int index0=0;
  while(index0<KEYWORDSNUM){
    if(strcmp(stringValue,keywords[index0])==0){
      return index0+N_CAR;
    }
    index0++;
  }
  return 0;
}

void freeTokenList(token_l** tokenList)
{
  if((*tokenList)==NULL){
    return;
  }
  token_l* tokenListTemp=(*tokenList)->next;
  freeTokenList(&tokenListTemp);
  free((*tokenList)->cur);
  (*tokenList)->cur=NULL;
  free((*tokenList));
  (*tokenList)=NULL;
}

tree_n* program(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  int type=(*curToken)->cur->type;
  tree_n* node=(tree_n*)calloc(1,sizeof(tree_n));
  if(type==N_LPAREN){
    node->son1=instructs(curToken,falseInfo);
  }else{
    falseInfoRecord("Expecting a '(' at a program begining?",falseInfo);
    assist1(curToken);
    return createFalseNode();
  }

  if((*curToken)->next->cur->type!=N_END){
    falseInfoRecord("Part of code out of scope!",falseInfo);
    assist1(curToken);
  }
  return node;
}

tree_n* instructs(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=(tree_n*)calloc(1,sizeof(tree_n));
  *curToken=(*curToken)->next;
  if((*curToken)==NULL){return node;}
  int type=(*curToken)->cur->type;
  if(type==')'){
    return node;
  }else{
    node->son1=instruct(curToken,falseInfo);
    node->brother=instructs(curToken,falseInfo);
  }
  return node;
}


tree_n* instruct(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  int type=(*curToken)->cur->type;
  tree_n* node;
  if(type!=N_LPAREN){
    falseInfoRecord("Expecting a '(' at an instruct begining?",falseInfo);
    assist1(curToken);
    return createFalseNode();
  }
  (*curToken)=(*curToken)->next;
  if((*curToken)==NULL){return NULL;}
  node=func(curToken,falseInfo);
  if(!assist2(curToken,&type)){return node;}

  if(type!=N_RPAREN){
    falseInfoRecord("Expecting a ')' at an instruct end",falseInfo);
    freeTreeNodes(&node);
    assist(curToken,1);
    return createFalseNode();
  }
  return node;
}


tree_n* func(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node;
  int type=(*curToken)->cur->type;
  if(type>=N_CAR && type<=N_GREATER){
    node=returnFunc(curToken,falseInfo);
  }else if(type==N_SET || type==N_PRINT){
    node=ioFunc(curToken,falseInfo);
  }else if(type==N_IF){
    node=ifFunc(curToken,falseInfo);
  }else if(type==N_WHILE){
    node=loopFunc(curToken,falseInfo);
  }else{
    falseInfoRecord("Was expecting a Function name ?",falseInfo);
    assist(curToken,1);
    return createFalseNode();
  }
  return node;
}

tree_n* returnFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node;
  int type=(*curToken)->cur->type;
  if(type>=N_CAR && type<=N_CONS){
    node=listFunc(curToken,falseInfo);
  }else if(type>=N_PLUS && type<=N_LENGTH){
    node=intFunc(curToken,falseInfo);
  }else if(type>=N_LESS && type<=N_GREATER){
    node=boolFunc(curToken,falseInfo);
  }else if(type>=N_MINUS && type<=N_DIVISION){
    node=intFunc(curToken,falseInfo);
  }else{
    falseInfoRecord("Was expecting a Function name ?",falseInfo);
    assist(curToken,1);
    return createFalseNode();
  }
  return node;
}

tree_n* ioFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=createNode(curToken);
  int type=(*curToken)->cur->type;
  if(type==N_PRINT){
    if(!assist2(curToken,&type)){return node;}

    if(type==N_STRING){
      node->son1=createNode(curToken);
    }else{
      node->son1=list(curToken,falseInfo);
    }
  }else if(type==N_SET){
    if(!assist2(curToken,&type)){return node;}
    if(type==N_VAR){
      node->son1=createNode(curToken);
    }else{
      falseInfoRecord("Expecting a Variable after set function?",falseInfo);
      node->son1=createFalseNode();
      assist(curToken,1);
      return node;
    }
    (*curToken)=(*curToken)->next;
    node->son2=list(curToken,falseInfo);
  }
  return node;
}

tree_n* listFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=createNode(curToken);
  int type=(*curToken)->cur->type;
  if(type==N_CAR || type==N_CDR || type== N_CONS){
    *curToken=(*curToken)->next;
    node->son1=list(curToken,falseInfo);
  }

  if(type==N_CONS){
    *curToken=(*curToken)->next;
    node->son2=list(curToken,falseInfo);
  }
  return node;
}

tree_n* intFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=createNode(curToken);
  int type=(*curToken)->cur->type;
  if((type>=N_PLUS&&type<=N_LENGTH)||(type>=N_MINUS && type<=N_DIVISION)){
    *curToken=(*curToken)->next;
    node->son1=list(curToken,falseInfo);
  }
  if(type==N_PLUS ||(type>=N_MINUS && type<=N_DIVISION)){
    *curToken=(*curToken)->next;
    node->son2=list(curToken,falseInfo);
  }
  return node;
}

tree_n* boolFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=createNode(curToken);
  int type=(*curToken)->cur->type;
  if(type==N_LESS || type==N_EQUAL || type==N_GREATER){
    *curToken=(*curToken)->next;
    node->son1=list(curToken,falseInfo);
    *curToken=(*curToken)->next;
    node->son2=list(curToken,falseInfo);
  }
  return node;
}

tree_n* ifFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=createNode(curToken);
  int type;
  if(!assist2(curToken,&type)){return NULL;}
  if(type!=N_LPAREN){
    falseInfoRecord("Was expecting a left parenthesis ?",falseInfo);
    node->son1=createFalseNode();
    assist1(curToken);
    return node;
  }
  if(!assist2(curToken,&type)){return node;}
  if(type>=N_LESS && type<=N_GREATER){
    node->son1=boolFunc(curToken,falseInfo);
  }else{
    falseInfoRecord("Was expecting a bool func name ?",falseInfo);
    node->son1=createFalseNode();
    assist(curToken,2);
    return node;
  }
  if(!assist2(curToken,&type)){return node;}
  if(type!=N_RPAREN){
    falseInfoRecord("Was expecting a right parenthesis ?",falseInfo);
    freeTreeNodes(&node->son1);
    node->son1=createFalseNode();
    assist1(curToken);
    return node;
  }
  ifFuncHelp1(curToken,node,falseInfo);
  return node;
}

void ifFuncHelp1(token_l** curToken,tree_n* node,char** falseInfo)
{
  if((*curToken)==NULL){return;}
  int type;
  if(!assist2(curToken,&type)){return;}
  if(type!=N_LPAREN){
    falseInfoRecord("Was expecting a left parenthesis ?",falseInfo);
    node->son2=createFalseNode();
    assist1(curToken);
    return;
  }
  node->son2=instructs(curToken,falseInfo);
  if(!assist2(curToken,&type)){return;}
  if(type!=N_LPAREN){
    falseInfoRecord("Was expecting a left parenthesis ?",falseInfo);
    node->son3=createFalseNode();
    assist1(curToken);
    return;
  }
  node->son3=instructs(curToken,falseInfo);
}

tree_n* loopFunc(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node=createNode(curToken);
  int type;
  if(!assist2(curToken,&type)){return NULL;}
  if(type!=N_LPAREN){
    falseInfoRecord("Was expecting a left parenthesis ?",falseInfo);
    node->son1=createFalseNode();
    assist1(curToken);
    return node;
  }
  if(!assist2(curToken,&type)){return node;}
  if(type>=N_LESS && type<=N_GREATER){
    node->son1=boolFunc(curToken,falseInfo);
  }else{
    falseInfoRecord("Was expecting a bool func name ?",falseInfo);
    node->son1=createFalseNode();
    assist(curToken,2);
    return node;
  }
  loopFuncHelp1(curToken,node,falseInfo);
  return node;
}

void loopFuncHelp1(token_l** curToken,tree_n* node,char** falseInfo)
{
  if((*curToken)==NULL){return;}
  int type;
  if(!assist2(curToken,&type)){return;}
  if(type!=N_RPAREN){
    falseInfoRecord("Was expecting a right parenthesis ?",falseInfo);
    freeTreeNodes(&node->son1);
    node->son1=createFalseNode();
    assist1(curToken);
    return;
  }
  if(!assist2(curToken,&type)){return;}
  if(type!=N_LPAREN){
    falseInfoRecord("Was expecting a left parenthesis ?",falseInfo);
    node->son2=createFalseNode();
    assist1(curToken);
    return;
  }
  node->son2=instructs(curToken,falseInfo);
}

void falseInfoRecord(char* str,char** falseInfo)
{
  if(*falseInfo==NULL){
    *falseInfo=(char*)calloc(MAX_LENGTH,sizeof(char));
    strcpy(*falseInfo,str);
  }
}

tree_n* list(token_l** curToken,char** falseInfo)
{
  if((*curToken)==NULL){return NULL;}
  tree_n* node;
  int type=(*curToken)->cur->type;
  if(type==N_VAR || type==N_LITERAL || type==N_NIL){
    node=createNode(curToken);
  }else if(type=='('){
    (*curToken)=(*curToken)->next;
    node=returnFunc(curToken,falseInfo);
    if(!assist2(curToken,&type)){return node;}

    if(type!=N_RPAREN){
      falseInfoRecord("Was expecting a right parenthesis ?",falseInfo);
      if(node->ifFalse==NULL){
        assist(curToken,1);
      }
      freeTreeNodes(&node);
      return createFalseNode();
    }
  }else{
    falseInfoRecord("Was expecting a list ?",falseInfo);
    assist(curToken,1);
    return createFalseNode();
  }
  return node;
}

tree_n* createNode(token_l** curToken)
{
  if((*curToken)==NULL){return NULL;}
  int type=(*curToken)->cur->type;
  tree_n* node=(tree_n*)calloc(1,sizeof(tree_n));
  node->type=(int*)calloc(1,sizeof(int));
  *(node->type)=type;
  if(type==N_LITERAL){
    strcpy(node->literalValue,(*curToken)->cur->stringValue);
  }else if(type==N_STRING){
    strcpy(node->stringValue,(*curToken)->cur->stringValue);
  }else if(type==N_VAR){
    strcpy(node->varName,(*curToken)->cur->stringValue);
  }
  return node;
}

tree_n* createFalseNode()
{
  tree_n* node1=(tree_n*)calloc(1,sizeof(tree_n));
  node1->ifFalse=(bool*)calloc(1,sizeof(bool));
  *(node1->ifFalse)=true;
  return node1;
}

void assist(token_l** curToken,int left)
{
  int right=0;
  while((*curToken)!=NULL && left!=right){
    if((*curToken)->next!=NULL && (*curToken)->next->cur->type==N_RPAREN){
      right++;
      if(right==left){
        return;
      }
    }else if((*curToken)->cur->type==N_LPAREN){
      left++;
    }
    (*curToken)=(*curToken)->next;
  }
}

void assist1(token_l** curToken)
{
  if((*curToken)==NULL){return;}
  while((*curToken)->cur->type==N_END){
    (*curToken)=(*curToken)->next;
  }
}

bool assist2(token_l** curToken,int* type)
{
  if((*curToken)==NULL){
    return false;
  }
  (*curToken)=(*curToken)->next;
  if((*curToken)==NULL){
    return false;
  }
  *type=(*curToken)->cur->type;
  return true;
}

#ifdef INTERP
tree_n* interpreter(tree_n* root)
{
  variables* vars=(variables*)calloc(1,sizeof(variables));
  dfs(root,vars,root);
  freeTreeNodes(&root);
  freeVariables(&vars);
  return root;
}


void dfs(tree_n* curNode,variables* vars,tree_n* root)
{
  if(curNode==NULL){
    return;
  }
  if(curNode->type==NULL){
    if(curNode->son1==NULL && curNode->brother==NULL){
      return;
    }
    falseFunc(curNode->son1,vars,root);
    dfs(curNode->son1,vars,root);
    dfs(curNode->brother,vars,root);
    return;
  }
  if(*(curNode->type)==N_PRINT){
    printInter(curNode,vars,root);
  }else if(*(curNode->type)==N_SET){
    setInter(curNode,vars,root);
  }else{
    dfsHelp1(curNode,vars,root);
  }
}

void dfsHelp1(tree_n* curNode,variables* vars,tree_n* root)
{
  int type=*(curNode->type);
  if(type==N_LESS){
    lessInter(curNode,vars,root);
  }else if(type==N_EQUAL){
    equalInter(curNode,vars,root);
  }else if(type==N_GREATER){
    greaterInter(curNode,vars,root);
  }else if(type==N_CAR){
    carInter(curNode,vars,root);
  }else if(type==N_CDR){
    cdrInter(curNode,vars,root);
  }else if(type==N_CONS){
    consInter(curNode,vars,root);
  }else if(type==N_IF){
    ifInter(curNode,vars,root);
  }else if(type==N_WHILE){
    whileInter(curNode,vars,root);
  }else if(type==N_LENGTH){
    lengthInter(curNode,vars,root);
  }else if(type==N_PLUS ||(type>=N_MINUS && type<=N_DIVISION)){
    plusInter(curNode,vars,root);
  }
}

void plusInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  tree_n* node2=curNode->son2;
  falseFunc(node2,vars,root);
  dfs(node2,vars,root);
  int val1=0,val2=0;
  plusIn0(node1,&val1,vars,root);
  plusIn0(node2,&val2,vars,root);
  if(*(curNode->type)==N_PLUS){
    curNode->intValue=val1+val2;
  }else if(*(curNode->type)==N_MINUS){
    curNode->intValue=val1-val2;
  }else if(*(curNode->type)==N_MULTIPLY){
    curNode->intValue=val1*val2;
  }else if(*(curNode->type)==N_DIVISION){
    curNode->intValue=val1/val2;
  }
}

void plusIn0(tree_n* node,int* val,variables* vars,tree_n* root)
{
  if(*(node->type)==N_VAR){
    plusIn1(node,val,vars,root);
  }else{
    plusIn2(node,val,vars,root);
  }
}

void plusIn1(tree_n* node,int* val,variables* vars,tree_n* root)
{
  int index;
  char str[MAX_LENGTH];
  varCheckHelp(&index,node,vars,root);
  int type=vars->varArray[index].type;
  if(type==N_LITERAL){
    liAndStrModify(vars->varArray[index].literalValue,str);
    plusIn3(str,vars,root);
    boolIn3(str,val);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    *val=vars->varArray[index].intValue;
  }else if(type==N_NIL){
    *val=0;
  }else if(type>=N_LESS && type<=N_GREATER){
    *val=(vars->varArray[index].boolValue?1:0);
  }else if(type>=N_CAR && type<=N_CONS){
    lisp_tostring(vars->varArray[index].l,str);
    plusIn3(str,vars,root);
    boolIn3(str,val);
  }else{
    printf("Expected a correct parameter for plus function\n");
    exitUnsuccess(vars,root);
  }
}

void plusIn2(tree_n* node,int* val,variables* vars,tree_n* root)
{
  char str[MAX_LENGTH];
  int type=*(node->type);
  if(type==N_LITERAL){
    liAndStrModify(node->literalValue,str);
    plusIn3(str,vars,root);
    boolIn3(str,val);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    *val=node->intValue;
  }else if(type==N_NIL){
    *val=0;
  }else if(type>=N_LESS && type<=N_GREATER){
    *val=(node->boolValue?1:0);
  }else if(type==N_CAR || type==N_CDR){
    lisp_tostring(node->l,str);
    plusIn3(str,vars,root);
    boolIn3(str,val);
  }else{
    printf("Expected a correct parameter for plus function\n");
    exitUnsuccess(vars,root);
  }
}

void plusIn3(char* str,variables* vars,tree_n* root)
{
  if(!literalCheckHelp1(str)){
    printf("Only single atom can be plus\n");
    exitUnsuccess(vars,root);
  }
}

void lengthInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  int type=*(node1->type);
  if(type==N_VAR){
    lengthIn1(curNode,node1,vars,root);
  }else if(type==N_LITERAL){
    char str[MAX_LENGTH];
    liAndStrModify(node1->literalValue,str);
    lisp* l=lisp_fromstring(str);
    curNode->intValue=lisp_length(l);
  }else if(type>=N_CAR && type<=N_CONS){
    curNode->intValue=lisp_length(node1->l);
  }else if((type>=N_PLUS&&type<=N_LENGTH)||(type>=N_MINUS&&type<=N_DIVISION)){
    curNode->intValue=0;
  }else if(type==N_NIL){
    curNode->intValue=0;
  }else{
    printf("Expected a correct parameter for lengtgh function\n");
    exitUnsuccess(vars,root);
  }
}

void lengthIn1(tree_n* curNode,tree_n* node1,variables* vars,tree_n* root)
{
  int index;
  varCheckHelp(&index,node1,vars,root);
  int type=vars->varArray[index].type;
  if(type==N_LITERAL){
    char str[MAX_LENGTH];
    liAndStrModify(vars->varArray[index].literalValue,str);
    lisp* l=lisp_fromstring(str);
    curNode->intValue=lisp_length(l);
  }else if(type>=N_CAR && type<=N_CONS){
    curNode->intValue=vars->varArray[index].intValue;
  }else if(type>=N_LESS && type<=N_GREATER){
    curNode->intValue=0;
  }else if(type==N_NIL){
    curNode->intValue=0;
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    curNode->intValue=0;
  }else{
    printf("Expected a correct parameter for lengtgh function\n");
    exitUnsuccess(vars,root);
  }
}

void whileInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  bool flag=false;
  whileIn1(node1,&flag,vars,root);
  while(flag){
    tree_n* node2=curNode->son2;
    falseFunc(node2,vars,root);
    dfs(node2,vars,root);
    dfs(node1,vars,root);
    whileIn1(node1,&flag,vars,root);
  }
}

void whileIn1(tree_n* node,bool* flag,variables* vars,tree_n* root)
{
  if(*(node->type)>=N_LESS && *(node->type)<=N_GREATER){
    *flag=node->boolValue;
  }else if(*(node->type)==N_VAR){
    int index;
    varCheckHelp(&index,node,vars,root);
    int type=vars->varArray[index].type;
    if(type>=N_LESS && type<=N_GREATER){
      *flag=vars->varArray[index].boolValue;
    }else{
      printf("Expected a correct parameter for while/if function\n");
      exitUnsuccess(vars,root);
    }
  }else{
    printf("Expected a correct parameter for while/if function\n");
    exitUnsuccess(vars,root);
  }
}

void ifInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  bool flag=false;
  whileIn1(node1,&flag,vars,root);
  if(flag){
    tree_n* node2=curNode->son2;
    falseFunc(node2,vars,root);
    dfs(node2,vars,root);
  }else{
    tree_n* node3=curNode->son3;
    falseFunc(node3,vars,root);
    dfs(node3,vars,root);
  }
}

void lessInter(tree_n* curNode,variables* vars,tree_n* root)
{
  int val1,val2;
  boolInter(curNode,&val1,&val2,vars,root);
  if(val1<val2){
    curNode->boolValue=true;
  }else{
    curNode->boolValue=false;
  }
}

void equalInter(tree_n* curNode,variables* vars,tree_n* root)
{
  int val1,val2;
  boolInter(curNode,&val1,&val2,vars,root);
  if(val1==val2){
    curNode->boolValue=true;
  }else{
    curNode->boolValue=false;
  }
}

void greaterInter(tree_n* curNode,variables* vars,tree_n* root)
{
  int val1,val2;
  boolInter(curNode,&val1,&val2,vars,root);
  if(val1>val2){
    curNode->boolValue=true;
  }else{
    curNode->boolValue=false;
  }
}

void boolInter(tree_n* curNode,int* val1,int* val2,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  tree_n* node2=curNode->son2;
  falseFunc(node2,vars,root);
  dfs(node2,vars,root);
  if(*(node1->type)==N_VAR){
    boolIn1(node1,val1,vars,root);
  }else{
    boolIn2(node1,val1,vars,root);
  }
  if(*(node2->type)==N_VAR){
    boolIn1(node2,val2,vars,root);
  }else{
    boolIn2(node2,val2,vars,root);
  }
}

void boolIn1(tree_n* node,int* val,variables* vars,tree_n* root)
{
  int index;
  char str[MAX_LENGTH];
  varCheckHelp(&index,node,vars,root);
  int type=vars->varArray[index].type;
  if(type==N_LITERAL){
    liAndStrModify(vars->varArray[index].literalValue,str);
    boolIn3(str,val);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    *val=vars->varArray[index].intValue;
  }else if(type==N_NIL){
    *val=0;
  }else if(type>=N_CAR && type<=N_CONS){
    lisp_tostring(vars->varArray[index].l,str);
    boolIn3(str,val);
  }else{
    printf("Expected a correct parameter for bool function\n");
    exitUnsuccess(vars,root);
  }
}

void boolIn2(tree_n* node,int* val,variables* vars,tree_n* root)
{
  int type=*(node->type);
  if(type==N_LITERAL){
    char str[MAX_LENGTH];
    liAndStrModify(node->literalValue,str);
    if(!literalCheck(str)){
      printf("Unsuitable literal\n");
      exitUnsuccess(vars,root);
    }
    boolIn3(str,val);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    *val=node->intValue;
  }else if(type==N_NIL){
    *val=0;
  }else if(type>=N_CAR && type<=N_CONS){
    char str[MAX_LENGTH];
    lisp_tostring(node->l,str);
    boolIn3(str,val);
  }else{
    printf("Expected a correct parameter for bool function\n");
    exitUnsuccess(vars,root);
  }
}

void boolIn3(char* literalValue,int* val)
{
  int len=strlen(literalValue);
  int res=0,sign=0,begin=0,end=0;
  if(begin<len-end && literalValue[begin]=='-'){
    sign=1;
    begin++;
  }
  while(begin<len-end && isdigit(literalValue[begin])){
    res=res*10+literalValue[begin]-'0';
    begin++;
  }
  if(begin==len-end && sign){
    *val=-res;
  }else if(begin==len-end){
    *val=res;
  }
}

void cdrInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  lisp* l1;
  consIn0(node1,&l1,vars,root);
  curNode->l=lisp_copy(lisp_cdr(l1));
}

void carInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  lisp* l1;
  consIn0(node1,&l1,vars,root);
  curNode->l=lisp_copy(lisp_car(l1));
}

void consInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  tree_n* node2=curNode->son2;
  falseFunc(node2,vars,root);
  dfs(node2,vars,root);

  lisp* l1,*l2;
  consIn0(node1,&l1,vars,root);
  consIn0(node2,&l2,vars,root);
  curNode->l=lisp_copy(lisp_cons(l1,l2));
}

void consIn0(tree_n* node,lisp** l,variables* vars,tree_n* root)
{
  if(*(node->type)==N_VAR){
    consIn1(node,l,vars,root);
  }else{
    consIn2(node,l,vars,root);
  }
}

void consIn1(tree_n* node,lisp** l,variables* vars,tree_n* root)
{
  int index;
  varCheckHelp(&index,node,vars,root);
  int type=vars->varArray[index].type;
  if(type==N_NIL){
    *l=NULL;
  }else if(type==N_LITERAL){
    char str[MAX_LENGTH];
    liAndStrModify(vars->varArray[index].literalValue,str);
    *l=lisp_fromstring(str);
  }else if(type>=N_CAR && type<=N_CONS){
    *l=vars->varArray[index].l;
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    *l=lisp_atom(vars->varArray[index].intValue);
  }else if(type>=N_LESS && type<=N_GREATER){
    *l=(vars->varArray[index].boolValue?lisp_atom(1):lisp_atom(0));
  }else{
    printf("Expected a correct parameter for cons function\n");
    exitUnsuccess(vars,root);
  }
}

void consIn2(tree_n* node,lisp** l,variables* vars,tree_n* root)
{
  int type=*(node->type);
  if(type==N_NIL){
    *l=NULL;
  }else if(type==N_LITERAL){
    char str[MAX_LENGTH];
    liAndStrModify(node->literalValue,str);
    if(!literalCheck(str)){
      printf("Unsuitable literal\n");
      exitUnsuccess(vars,root);
    }
    *l=lisp_fromstring(str);
  }else if(type>=N_CAR && type<=N_CONS ){
    *l=node->l;
  }else if(type>=N_LESS && type<=N_GREATER){
    *l=(node->boolValue?lisp_atom(1):lisp_atom(0));
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    *l=lisp_atom(node->intValue);
  }else{
    printf("Expected a correct parameter for cons function\n");
    exitUnsuccess(vars,root);
  }
}

void printInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  if(*(node1->type)==N_VAR){
    printIn1(node1,vars,root);
  }else{
    printIn2(node1,vars,root);
  }
}

void printIn1(tree_n* node1,variables* vars,tree_n* root)
{
  char str[MAX_LENGTH];
  int index;
  varCheckHelp(&index,node1,vars,root);
  int type=vars->varArray[index].type;
  if(type==N_LITERAL){
    liAndStrModify(vars->varArray[index].literalValue,str);
    printf("%s\n",str);
  }else if(type==N_NIL){
    printf("NIL\n");
  }else if(type>=N_CAR && type<=N_CONS){
    lisp_tostring(vars->varArray[index].l,str);
    printf("%s\n",str);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    printf("%d\n",vars->varArray[index].intValue);
  }else if(type>=N_LESS && type<=N_GREATER){
    if(vars->varArray[index].boolValue==true){
      printf("true\n");
    }else{
      printf("false\n");
    }
  }
}

void printIn2(tree_n* node1,variables* vars,tree_n* root)
{
  char str[MAX_LENGTH];
  int type=*(node1->type);
  if(type==N_STRING){
    liAndStrModify(node1->stringValue,str);
    printf("%s\n",str);
  }else if(type==N_LITERAL){
    liAndStrModify(node1->literalValue,str);
    if(!literalCheck(str)){
      printf("Unsuitable literal\n");
      exitUnsuccess(vars,root);
    }
    printf("%s\n",str);
  }else if(type==N_NIL){
    printf("NIL\n");
  }else if(type>=N_CAR && type<=N_CONS){
    lisp_tostring(node1->l,str);
    printf("%s\n",str);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    printf("%d\n",node1->intValue);
  }else if(type>=N_LESS && type<=N_GREATER){
    if(node1->boolValue==true){printf("true\n");}
    else{printf("false\n");}
  }else{
    printf("Expected a correct parameter for print function\n");
    exitUnsuccess(vars,root);
  }
}


void setInter(tree_n* curNode,variables* vars,tree_n* root)
{
  tree_n* node1=curNode->son1;
  falseFunc(node1,vars,root);
  dfs(node1,vars,root);
  tree_n* node2=curNode->son2;
  falseFunc(node2,vars,root);
  dfs(node2,vars,root);
  if(isVarExist(node1->varName,vars)){
    setIn1(node1,node2,vars,root);
  }else{
    setIn2(node1,node2,vars,root);
  }
}

void setIn1(tree_n* node1,tree_n* node2,variables* vars,tree_n* root)
{
  int index=getVarIndex(node1->varName,vars);
  int type=*(node2->type);
  variable* v=&(vars->varArray[index]);
  v->type=*(node2->type);
  if(type==N_LITERAL){
    strcpy(v->literalValue,node2->literalValue);
  }else if(type>=N_LESS && type<=N_GREATER){
    v->boolValue=node2->boolValue;
  }else if(type>=N_CAR && type<=N_CONS){
    lisp_free(&(v->l));
    v->l=lisp_copy(node2->l);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    v->intValue=node2->intValue;
  }else if(type==N_VAR){
    int index1;
    varCheckHelp(&index1,node2,vars,root);
    variable* v1=&(vars->varArray[index1]);
    v->type=v1->type;
    v->boolValue=v1->boolValue;
    v->intValue=v1->intValue;
    strcpy(v->stringValue,v1->stringValue);
    strcpy(v->literalValue,v1->literalValue);
    v->l=lisp_copy(v1->l);
  }
}

void setIn2(tree_n* node1,tree_n* node2,variables* vars,tree_n* root)
{
  variable* oneVar=(variable*)calloc(1,sizeof(variable));
  strcpy(oneVar->varName,node1->varName);
  oneVar->type=*(node2->type);
  if(*(node2->type)==N_VAR){
    int index1;
    varCheckHelp(&index1,node2,vars,root);
    oneVar->type=vars->varArray[index1].type;
    oneVar->boolValue=vars->varArray[index1].boolValue;
    oneVar->intValue=vars->varArray[index1].intValue;
    strcpy(oneVar->stringValue,vars->varArray[index1].stringValue);
    strcpy(oneVar->literalValue,vars->varArray[index1].literalValue);
    oneVar->l=lisp_copy(vars->varArray[index1].l);
  }else{
    setIn3(oneVar,node2,vars,root);
  }
  vars->varArray[vars->len++]=*(oneVar);
}

void setIn3(variable* oneVar,tree_n* node2,variables* vars,tree_n* root)
{
  int type=*(node2->type);
  if(type==N_LITERAL){
    char str[MAX_LENGTH];
    liAndStrModify(node2->literalValue,str);
    if(!literalCheck(str)){
      printf("Unsuitable literal\n");
      exitUnsuccess(vars,root);
    }
    strcpy(oneVar->literalValue,node2->literalValue);
  }else if(type>=N_LESS && type<=N_GREATER){
    oneVar->boolValue=node2->boolValue;
  }else if(type>=N_CAR && type<=N_CONS){
    oneVar->l=lisp_copy(node2->l);
  }else if((type>=N_PLUS&&type<=N_LENGTH)
  ||(type>=N_MINUS&&type<=N_DIVISION)){
    oneVar->intValue=node2->intValue;
  }
}

bool isVarExist(char* name,variables* vars)
{
  for(int i=0;i<vars->len;i++){
    if(strcmp(vars->varArray[i].varName,name)==0){
      return true;
    }
  }
  return false;
}

int getVarIndex(char* name,variables* vars)
{
  for(int i=0;i<vars->len;i++){
    if(strcmp(vars->varArray[i].varName,name)==0){
      return i;
    }
  }
  return -1;
}

void falseFunc(tree_n* node,variables* vars,tree_n* root)
{
  if(node->ifFalse!=NULL && *(node->ifFalse)){
    exitUnsuccess(vars,root);
  }
}

void freeVariables(variables** vars)
{
  for(int i=0;i<(*vars)->len;i++){
    if((*vars)->varArray[i].l!=NULL){
      lisp_free(&((*vars)->varArray[i].l));
    }
  }
  free((*vars));
  (*vars)=NULL;
}

bool literalCheck(char* str)
{
  return literalCheckHelp1(str) || literalCheckHelp2(str);
}

bool literalCheckHelp1(char* str)
{
  int len=strlen(str);
  int index=0;
  if(str[0]=='-'){index++;}
  while(index<len){
    if(!isdigit(str[index])){return false;}
    index++;
  }
  return true;
}

bool literalCheckHelp2(char* str)
{
  int left=0,right=0,len=0;
  len=strlen(str);
  for(int i=0;i<len;i++){
    if(str[i]!='('&&str[i]!=')'
    &&!isdigit(str[i])&&str[i]!='-'&&str[i]!=' '){
      return false;
    }
    if(str[i]==N_LPAREN){left++;}
    if(str[i]==N_RPAREN){right++;}
    if(str[i]=='-' || isdigit(str[i])){
      i++;
      while(i<len && isdigit(str[i])){i++;}
      if(left==0){return false;}
      else if(i<len &&
        (str[i]==' '||str[i]==N_RPAREN||str[i]==N_LPAREN)){
        i--;
      }
      else{return false;}
    }
    if(right>left){return false;}
    if(right==left && i<len-1){return false;}
  }
  if(right!=left){return false;}
  return true;
}

void liAndStrModify(char* bef,char* str)
{
  int len=strlen(bef);
  for(int i=1;i<len-1;i++){
    str[i-1]=bef[i];
  }
  str[len-2]='\0';
}

void varCheckHelp(int* index,tree_n* node,variables* vars,tree_n* root)
{
  if(isVarExist(node->varName,vars)){
    *index=getVarIndex(node->varName,vars);
  }else{
    printf("Variable %s does not been initialized\n",node->varName);
    exitUnsuccess(vars,root);
  }
}

void exitUnsuccess(variables* vars,tree_n* root)
{
  freeTreeNodes(&root);
  freeVariables(&vars);
  exit(1);
}
#endif

void freeTreeNodes(tree_n** curNode)
{
  if(curNode==NULL || (*curNode)==NULL){return;}
  tree_n* node=*curNode;
  freeTreeNodes(&(node->son1));
  freeTreeNodes(&(node->son2));
  freeTreeNodes(&(node->son3));
  freeTreeNodes(&(node->brother));
  if(node->type!=NULL){
    free(node->type);
    node->type=NULL;
  }
  #ifdef INTERP
  if(node->l!=NULL){
    lisp_free(&(node->l));
    node->l=NULL;
  }
  #endif
  if(node->ifFalse!=NULL){
    free(node->ifFalse);
    node->ifFalse=NULL;
  }
  freeTreeNodesHelp(node->son1);
  freeTreeNodesHelp(node->son2);
  freeTreeNodesHelp(node->son3);
  freeTreeNodesHelp(node->brother);
  *curNode=NULL;
}


void freeTreeNodesHelp(tree_n* curNode)
{
  if(curNode!=NULL){
    free(curNode);
    curNode=NULL;
  }
}

void whiteTest()
{
  char keywords[KEYWORDSNUM][KEYWORDLEN];
  initKeyWord(keywords);
  //Test ScanOneToken(token_n** token,FILE* fp,char keywords[][KEYWORDLEN]);
  char* fileName="z_test4.ncl";
  /*
  (
    (SET F '(2)')
    (PRINT '2')
    (PRINT "ABC")
    (CAR (CDR (CONS '(1)' '(22)')))
    (LENGTH '(1)')
    (PLUS '1' '2')
    1
  )
  */
  FILE* fp=fopen(fileName,"r");
  if(fp!=NULL){
    token_n* token=(token_n*)calloc(1,sizeof(token_n));
    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_SET);
    assert(ScanOneToken(&token,fp,keywords)==N_VAR);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_PRINT);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_PRINT);
    assert(ScanOneToken(&token,fp,keywords)==N_STRING);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_CAR);
    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_CDR);
    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_CONS);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_LENGTH);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_LPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_PLUS);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_LITERAL);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);

    assert(ScanOneToken(&token,fp,keywords)==N_UNKNOWN);
    assert(ScanOneToken(&token,fp,keywords)==N_RPAREN);
    assert(ScanOneToken(&token,fp,keywords)==N_END);
    free(token);
  }
  fclose(fp);
  //Test isKeyWord(char stringValue[],char keywords[][KEYWORDLEN]);
  assert(isKeyWord("CAR",keywords)==N_CAR);
  assert(isKeyWord("CDR",keywords)==N_CDR);
  assert(isKeyWord("CONS",keywords)==N_CONS);
  assert(isKeyWord("PLUS",keywords)==N_PLUS);
  assert(isKeyWord("LENGTH",keywords)==N_LENGTH);
  assert(isKeyWord("LESS",keywords)==N_LESS);
  assert(isKeyWord("GREATER",keywords)==N_GREATER);
  assert(isKeyWord("EQUAL",keywords)==N_EQUAL);
  assert(isKeyWord("SET",keywords)==N_SET);
  assert(isKeyWord("PRINT",keywords)==N_PRINT);
  assert(isKeyWord("IF",keywords)==N_IF);
  assert(isKeyWord("NIL",keywords)==N_NIL);
  assert(isKeyWord("SETA",keywords)==0);
  assert(isKeyWord("ASDTA",keywords)==0);
  assert(isKeyWord("SSKHJGDHJA",keywords)==0);
  assert(isKeyWord("GHDHJHA",keywords)==0);
  assert(isKeyWord("DHUH",keywords)==0);
  assert(isKeyWord("SUHF",keywords)==0);

  //token_l* initLexicalAnalysis(char* fileName);
  //tree_n* program(token_l** curToken,char** falseInfo);
  char* falseInfo=NULL;
  token_l* tokenList1=initLexicalAnalysis("z_test1.ncl");
  tree_n* node=program(&tokenList1,&falseInfo);
  freeTreeNodes(&node);
  assert(node==NULL);
  freeTokenList(&tokenList1);
  assert(tokenList1==NULL);
  assert(falseInfo!=NULL);
  free(falseInfo);
  falseInfo=NULL;

  token_l* tokenList2=initLexicalAnalysis("z_test2.ncl");
  node=program(&tokenList2,&falseInfo);
  freeTreeNodes(&node);
  assert(node==NULL);
  freeTokenList(&tokenList2);
  assert(tokenList2==NULL);
  assert(falseInfo==NULL);

  token_l* tokenList3=initLexicalAnalysis("z_test3.ncl");
  node=program(&tokenList3,&falseInfo);
  freeTreeNodes(&node);
  assert(node==NULL);
  freeTokenList(&tokenList3);
  assert(tokenList3==NULL);
  assert(falseInfo==NULL);

  /*
  bool assist2(token_l** curToken,int* type)
  (
    (SET F '(2)')
    (PRINT '2')
    (PRINT "ABC")
    (CAR (CDR (CONS '(1)' '(22)')))
    (LENGTH '(1)')
    (PLUS '1' '2')
    1
  )
  */
  token_l* tokenList4=initLexicalAnalysis("z_test4.ncl");
  token_l* curToken=tokenList4;
  int type;
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==true);
  assert(assist2(&curToken,&type)==false);
  freeTokenList(&tokenList4);
  assert(tokenList4==NULL);

  #ifdef INTERP
  /*
  bool isVarExist(char* name,variables* vars);
  int getVarIndex(char* name,variables* vars);
  */
  variables* vars=(variables*)calloc(1,sizeof(variables));
  vars->len=15;
  strcpy(vars->varArray[0].varName,"A");
  strcpy(vars->varArray[1].varName,"B");
  strcpy(vars->varArray[2].varName,"C");
  strcpy(vars->varArray[3].varName,"D");
  strcpy(vars->varArray[4].varName,"E");
  strcpy(vars->varArray[5].varName,"F");
  strcpy(vars->varArray[6].varName,"G");
  strcpy(vars->varArray[7].varName,"H");
  strcpy(vars->varArray[8].varName,"I");
  strcpy(vars->varArray[9].varName,"J");
  strcpy(vars->varArray[10].varName,"K");
  strcpy(vars->varArray[11].varName,"L");
  strcpy(vars->varArray[12].varName,"M");
  strcpy(vars->varArray[13].varName,"N");
  strcpy(vars->varArray[14].varName,"O");
  assert(isVarExist("A",vars)==true);
  assert(isVarExist("B",vars)==true);
  assert(isVarExist("C",vars)==true);
  assert(isVarExist("D",vars)==true);
  assert(isVarExist("E",vars)==true);
  assert(isVarExist("F",vars)==true);
  assert(isVarExist("G",vars)==true);
  assert(isVarExist("H",vars)==true);
  assert(isVarExist("I",vars)==true);
  assert(isVarExist("J",vars)==true);
  assert(isVarExist("K",vars)==true);
  assert(isVarExist("L",vars)==true);
  assert(isVarExist("M",vars)==true);
  assert(isVarExist("N",vars)==true);
  assert(isVarExist("O",vars)==true);
  assert(isVarExist("P",vars)==false);
  assert(isVarExist("Q",vars)==false);
  assert(isVarExist("R",vars)==false);
  assert(isVarExist("S",vars)==false);
  assert(isVarExist("T",vars)==false);
  assert(isVarExist("U",vars)==false);
  assert(isVarExist("V",vars)==false);
  assert(isVarExist("W",vars)==false);
  assert(isVarExist("X",vars)==false);
  assert(isVarExist("Y",vars)==false);
  assert(isVarExist("Z",vars)==false);
  assert(getVarIndex("A",vars)==0);
  assert(getVarIndex("B",vars)==1);
  assert(getVarIndex("C",vars)==2);
  assert(getVarIndex("D",vars)==3);
  assert(getVarIndex("E",vars)==4);
  assert(getVarIndex("F",vars)==5);
  assert(getVarIndex("G",vars)==6);
  assert(getVarIndex("H",vars)==7);
  assert(getVarIndex("I",vars)==8);
  assert(getVarIndex("J",vars)==9);
  assert(getVarIndex("K",vars)==10);
  assert(getVarIndex("L",vars)==11);
  assert(getVarIndex("M",vars)==12);
  assert(getVarIndex("N",vars)==13);
  assert(getVarIndex("O",vars)==14);
  assert(getVarIndex("P",vars)==-1);
  assert(getVarIndex("Q",vars)==-1);
  assert(getVarIndex("R",vars)==-1);
  assert(getVarIndex("S",vars)==-1);
  assert(getVarIndex("T",vars)==-1);
  assert(getVarIndex("U",vars)==-1);
  assert(getVarIndex("V",vars)==-1);
  assert(getVarIndex("W",vars)==-1);
  assert(getVarIndex("X",vars)==-1);
  assert(getVarIndex("Y",vars)==-1);
  assert(getVarIndex("Z",vars)==-1);
  freeVariables(&vars);
  assert(vars==NULL);


  /*
  bool literalCheck(char* str);
  bool literalCheckHelp1(char* str);
  bool literalCheckHelp2(char* str);
  */
  assert(literalCheckHelp1("1")==true);
  assert(literalCheckHelp1("-1")==true);
  assert(literalCheckHelp1("-123")==true);
  assert(literalCheckHelp1("123")==true);
  assert(literalCheckHelp1("123a")==false);
  assert(literalCheckHelp1("a123-")==false);
  assert(literalCheckHelp1("a123 ")==false);

  assert(literalCheckHelp2("1")==false);
  assert(literalCheckHelp2("-1")==false);
  assert(literalCheckHelp2("-123")==false);
  assert(literalCheckHelp2("123")==false);
  assert(literalCheckHelp2("123a")==false);
  assert(literalCheckHelp2("a123-")==false);
  assert(literalCheckHelp2("a123 ")==false);
  assert(literalCheckHelp2("(1)1")==false);
  assert(literalCheckHelp2("1(1)")==false);
  assert(literalCheckHelp2("((1)")==false);
  assert(literalCheckHelp2("((11)))")==false);
  assert(literalCheckHelp2("(((11)))")==true);
  assert(literalCheckHelp2("(((-11)))")==true);
  assert(literalCheckHelp2("(((--11)))")==false);
  assert(literalCheckHelp2("(1 2(3 -5(6)))")==true);
  assert(literalCheckHelp2("(1 2(3 -5(6 7)99)99)")==true);
  assert(literalCheckHelp2("(1 2(3 -5(6 a7)99)99)")==false);
  assert(literalCheckHelp2("((1 3)()")==false);
  assert(literalCheckHelp2("()(1)")==false);

  assert(literalCheck("1")==true);
  assert(literalCheck("-1")==true);
  assert(literalCheck("-123")==true);
  assert(literalCheck("123")==true);
  assert(literalCheck("123a")==false);
  assert(literalCheck("a123-")==false);
  assert(literalCheck("a123 ")==false);
  assert(literalCheck("(1)1")==false);
  assert(literalCheck("1(1)")==false);
  assert(literalCheck("((1)")==false);
  assert(literalCheck("((11)))")==false);
  assert(literalCheck("(((11)))")==true);
  assert(literalCheck("(((-11)))")==true);
  assert(literalCheck("(((--11)))")==false);
  assert(literalCheck("(1 2(3 -5(6)))")==true);
  assert(literalCheck("(1 2(3 -5(6 7)99)99)")==true);
  assert(literalCheck("(1 2(3 -5(6 a7)99)99)")==false);
  assert(literalCheck("((1 3)()")==false);
  assert(literalCheck("()(1)")==false);
  #endif
}

int main(int args,char* argv[])
{
  whiteTest();
  if(args!=2){
    printf("Parameter missing!\n");
  }
  token_l* tokenList=initLexicalAnalysis(argv[1]);
  char* falseInfo=NULL;
  tree_n* root=program(&tokenList,&falseInfo);
  freeTokenList(&tokenList);
  #ifdef INTERP
    interpreter(root);
  #else
    if(falseInfo==NULL){
      printf("Parsed ok!\n");
    }else{
      printf("%s\n",falseInfo);
      free(falseInfo);
    }
  #endif
  freeTreeNodes(&root);
  return 0;
}
