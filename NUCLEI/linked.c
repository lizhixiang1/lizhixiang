#include "lisp.h"
struct lisp{
  lisp* car;
  lisp* cdr;
  int* val;
 };
int lisp_length_backTrace(const lisp* l);
void lisp_tostring_backTrace(const lisp* l,char* str,bool judge);
void lisp_tostring_help1(const lisp* l,char* str);
void lisp_tostring_help2(const lisp* l,char* str,bool judge);
lisp* lisp_fromstring_backtrace(const char* str,int* index);
void lisp_fromstring_help(const char* str,int* index,lisp* lisps[],int* length);
#define LISTSTRLEN 1000

lisp* lisp_atom(const atomtype a)
{
  lisp* res=(lisp*)ncalloc(1,sizeof(lisp));
  res->val=(int*)ncalloc(1,sizeof(int));
  *(res->val)=a;
  return res;
}

lisp* lisp_cons(const lisp* l1,  const lisp* l2)
{
  if(l1==NULL && l2==NULL){
    return NULL;
  }
  lisp* res=(lisp*)ncalloc(1,sizeof(lisp));
  res->car=(lisp*)l1;
  res->cdr=(lisp*)l2;
  return res;
}

lisp* lisp_car(const lisp* l)
{
  return l==NULL?NULL:(lisp*)l->car;
}

lisp* lisp_cdr(const lisp* l)
{
  return l==NULL?NULL:(lisp*)l->cdr;
}

atomtype lisp_getval(const lisp* l)
{
  if(l==NULL){
    return 0;
  }
  if(l->val!=NULL){
    return *(l->val);
  }
  return 0;
}

bool lisp_isatomic(const lisp* l)
{
  if(l!=NULL && l->val!=NULL){
    return true;
  }
  return false;
}

lisp* lisp_copy(const lisp* l)
{
  if(l==NULL){
    return NULL;
  }
  lisp* res=(lisp*)ncalloc(1,sizeof(lisp));
  res->car=lisp_copy((lisp*)l->car);
  res->cdr=lisp_copy((lisp*)l->cdr);
  if(l->val!=NULL){
    res->val=(int*)ncalloc(1,sizeof(int));
    *(res->val)=*(l->val);
  }
  return res;
}

int lisp_length(const lisp* l)
{
  if(l==NULL || l->val!=NULL){
    return 0;
  }
  return lisp_length_backTrace(l->cdr)+1;
}

int lisp_length_backTrace(const lisp* l)
{
  if(l==NULL){
    return 0;
  }
  return lisp_length_backTrace(l->cdr)+1;
}

void lisp_tostring(const lisp* l, char* str)
{
    if(l==NULL){
      str[0]='(',str[1]=')',str[2]='\0';
      return;
    }
    str[0]='\0';
    lisp_tostring_backTrace(l,str,true);
    str[strlen(str)-1]='\0';
}

void lisp_tostring_backTrace(const lisp* l,char* str,bool judge)
{
  if(l==NULL){
    return;
  }
  lisp_tostring_help1(l,str);
  lisp_tostring_help2(l,str,judge);
}

void lisp_tostring_help1(const lisp* l,char* str)
{
  if(l->val!=NULL){
    int temp=*(l->val);
    if(temp<0){
      strcat(str,"-\0");
      temp=(-temp);
    }
    char num[LISTSTRLEN]={'\0'};
    snprintf(num,LISTSTRLEN,"%d",temp);
    strcat(str,num);
    strcat(str," \0");
  }
}

void lisp_tostring_help2(const lisp* l,char* str,bool judge)
{
  if((lisp*)l->car!=NULL){
    if(judge){
      strcat(str,"(\0");
    }
    lisp_tostring_backTrace((lisp*)l->car,str,true);
    lisp_tostring_backTrace((lisp*)l->cdr,str,false);
    if(judge){
      str[strlen(str)-1]='\0';
      strcat(str,") ");
    }
  }
}

void lisp_free(lisp** l)
{
  if(l==NULL || *l==NULL){
    return;
  }
  lisp* cur=*l;
  lisp_free(&(cur->car));
  lisp_free(&(cur->cdr));
  free(cur->val);
  free(*l);
  *l=NULL;
}

lisp* lisp_fromstring(const char* str)
{
  if(str[0]=='(' && str[1]==')' && str[2]=='\0'){
    return NULL;
  }
  if(str[0]!='('){
    int index=0,val=0,len=0;
    len=strlen(str);
    while(index<len && str[index]>='0' && str[index]<='9'){
      val=val*10+(str[index]-'0');
      (index)++;
    }
    return lisp_atom(val);
  }
  int index=1;
  lisp* res=lisp_fromstring_backtrace(str,&index);
  return res;
}

lisp* lisp_fromstring_backtrace(const char* str,int* index)
{
  lisp* lisps[LISTSTRLEN]={NULL};
  int length=0;
  while(str[*index]!=')'){
    if(str[*index]=='('){
      (*index)++;
      lisps[length]=lisp_fromstring_backtrace(str,index);
      length++;
    }else{
      lisp_fromstring_help(str,index,lisps,&length);
    }
  }
  (*index)++;
  lisp* res=NULL;
  for(int i=length-1;i>=0;i--){
    res=lisp_cons(lisps[i],res);
  }
  return res;
}

void lisp_fromstring_help(const char* str,int* index,lisp* lisps[],int* length)
{
  int val=0;
  bool flag1=false,flag2=false;
  if(str[*index]=='-'){
    flag1=true;
    (*index)++;
  }
  while(str[*index]>='0' && str[*index]<='9'){
    val=val*10+(str[*index]-'0');
    (*index)++;
    flag2=true;
  }
  if(flag2){
    lisps[*length]=(lisp*)ncalloc(1,sizeof(lisp));
    lisps[*length]->val=(int*)ncalloc(1,sizeof(int));
    *(lisps[*length]->val)=(flag1?(-val):val);
    (*length)++;
  }else{
      (*index)++;
  }
}

lisp* lisp_list(const int n, ...)
{
  va_list args;
  va_start(args,n);
  lisp* res=NULL;
  lisp* lisps[LISTSTRLEN];
  for(int i=n-1;i>=0;i--){
    lisps[i]=va_arg(args,lisp*);
  }
  for(int i=0;i<n;i++){
    res=lisp_cons(lisps[i],res);
  }
  va_end(args);
  return res;
}

void lisp_reduce(void (*func)(lisp* l, atomtype* n), lisp* l, atomtype* acc)
{
  if(l==NULL){
    return;
  }
  lisp* car=(lisp*)l->car;
  lisp* cdr=(lisp*)l->cdr;
  int* val=l->val;
  if(car==NULL && cdr==NULL && val!=NULL){
    func(l,acc);
  }
  lisp_reduce(func,car,acc);
  lisp_reduce(func,cdr,acc);
}

void test()
{
  char str[LISTSTRLEN];

  assert(lisp_isatomic(NULL)==false);
  lisp* a1 = lisp_atom(999);
  assert(lisp_length(a1)==0);
  assert(lisp_isatomic(a1)==true);
  lisp_free(&a1);
  assert(a1==NULL);

  lisp* l1 = lisp_cons(lisp_atom(-999),NULL);
  lisp_tostring(l1, str);
  assert(strcmp(str, "(-999)")==0);
  assert(lisp_length(l1)==1);
  assert(lisp_isatomic(l1)==false);
  assert(lisp_isatomic(l1->car)==true);
  lisp_free(&l1);
  assert(l1==NULL);

  lisp* l2 = lisp_cons(lisp_atom(2),NULL);
  assert(l2);
  assert(lisp_length(l2)==1);
  lisp_tostring(l2, str);
  assert(strcmp(str, "(2)")==0);

  lisp* l3 = lisp_cons(l2, NULL);
  assert(l3);
  assert(lisp_length(l3)==1);
  lisp_tostring(l3, str);
  assert(strcmp(str, "((2))")==0);

  lisp* l4 = lisp_cons(l3, NULL);
  assert(l4);
  assert(lisp_length(l4)==1);
  lisp_tostring(l4, str);
  assert(strcmp(str, "(((2)))")==0);

  lisp* l5 = lisp_cons(lisp_atom(0), l4);
  assert(l5);
  assert(lisp_length(l5)==2);
  lisp_tostring(l5, str);
  assert(strcmp(str, "(0 ((2)))")==0);
  lisp_free(&l5);
  assert(!l5);

  char inp[4][LISTSTRLEN] = {"()", "(((((1)))))",
   "(1 2 3 45 -11023 (((((1))))))", "(0)"};
  for(int i=0; i<4; i++){
    lisp* f1 = lisp_fromstring(inp[i]);
    lisp_tostring(f1, str);
    assert(strcmp(str, inp[i])==0);
    lisp_free(&f1);
    assert(!f1);
  }
}
