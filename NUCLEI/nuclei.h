#include "lisp.h"
#include <ctype.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define N_LPAREN '('
#define N_RPAREN ')'
#define N_CAR 257
#define N_CDR 258
#define N_CONS 259
#define N_PLUS 260
#define N_LENGTH 261
#define N_LESS 262
#define N_EQUAL 263
#define N_GREATER 264
#define N_SET 265
#define N_PRINT 266
#define N_IF 267
#define N_WHILE 268
#define N_NIL 269
#define N_MINUS 270
#define N_MULTIPLY 271
#define N_DIVISION 272
#define N_VAR 273
#define N_STRING 274
#define N_LITERAL 275
#define N_END 276
#define N_UNKNOWN 277
#define MAX_LENGTH 1000
#define KEYWORDSNUM 16
#define KEYWORDLEN 10
#define MAX_VARNUM 200
#define MAX_VARLEN 20
#define MAX_FALSEINFO 100

struct token_n{
  int type;
  char stringValue[MAX_LENGTH];
};
typedef struct token_n token_n;

struct token_l{
  token_n* cur;
  struct token_l* next;
};
typedef struct token_l token_l;

struct tree_n{
  int* type;
  bool* ifFalse;
  int varType;
  char varName[MAX_VARLEN];
  char stringValue[MAX_LENGTH];
  char literalValue[MAX_LENGTH];
  bool boolValue;
  int intValue;
  lisp* l;
  struct tree_n* brother;
  struct tree_n* son1;
  struct tree_n* son2;
  struct tree_n* son3;
};
typedef struct tree_n tree_n;

struct variable{
  char varName[MAX_VARLEN];
  int type;
  char stringValue[MAX_LENGTH];
  char literalValue[MAX_LENGTH];
  bool boolValue;
  int intValue;
  lisp* l;
};
typedef struct variable variable;

struct variables{
  variable varArray[MAX_VARNUM];
  int len;
};
typedef struct variables variables;

token_l* initLexicalAnalysis(char* fileName);
int ScanOneToken(token_n** token,FILE* fp,char keywords[][KEYWORDLEN]);
void initKeyWord(char keywords[][KEYWORDLEN]);
int isKeyWord(char stringValue[],char keywords[][KEYWORDLEN]);
void freeTokenList();
void checkK_V(token_n** token,FILE* fp,char ch,char keywords[][KEYWORDLEN]);
void checkLiteral(token_n** token,FILE* fp,char ch);
void checkString(token_n** token,FILE* fp,char ch);
void freeTokenList(token_l** tokenList);
tree_n* program(token_l** curToken,char** falseInfo);
tree_n* instructs(token_l** curToken,char** falseInfo);
tree_n* instruct(token_l** curToken,char** falseInfo);
tree_n* func(token_l** curToken,char** falseInfo);
tree_n* returnFunc(token_l** curToken,char** falseInfo);
tree_n* ioFunc(token_l** curToken,char** falseInfo);
tree_n* listFunc(token_l** curToken,char** falseInfo);
tree_n* intFunc(token_l** curToken,char** falseInfo);
tree_n* boolFunc(token_l** curToken,char** falseInfo);
tree_n* setFunc(token_l** curToken,char** falseInfo);
tree_n* printFunc(token_l** curToken,char** falseInfo);
tree_n* ifFunc(token_l** curToken,char** falseInfo);
void ifFuncHelp1(token_l** curToken,tree_n* node,char** falseInfo);
tree_n* loopFunc(token_l** curToken,char** falseInfo);
void loopFuncHelp1(token_l** curToken,tree_n* node,char** falseInfo);
tree_n* list(token_l** curToken,char** falseInfo);
void falseInfoRecord(char* str,char** falseInfo);
tree_n* createNode(token_l** curToken);
tree_n* createFalseNode();
void assist(token_l** curToken,int left);
void assist1(token_l** curToken);
bool assist2(token_l** curToken,int* type);
tree_n* interpreter(tree_n* root);
void dfs(tree_n* curNode,variables* vars,tree_n* root);
void dfsHelp1(tree_n* curNode,variables* vars,tree_n* root);
void plusInter(tree_n* curNode,variables* vars,tree_n* root);
void plusIn0(tree_n* node,int* val,variables* vars,tree_n* root);
void plusIn1(tree_n* node,int* val,variables* vars,tree_n* root);
void plusIn2(tree_n* node,int* val,variables* vars,tree_n* root);
void lengthInter(tree_n* curNode,variables* vars,tree_n* root);
void plusIn3(char* str,variables* vars,tree_n* root);
void lengthIn1(tree_n* curNode,tree_n* node1,variables* vars,tree_n* root);
void whileInter(tree_n* curNode,variables* vars,tree_n* root);
void whileIn1(tree_n* node,bool* flag,variables* vars,tree_n* root);
void ifInter(tree_n* curNode,variables* vars,tree_n* root);
void lessInter(tree_n* curNode,variables* vars,tree_n* root);
void equalInter(tree_n* curNode,variables* vars,tree_n* root);
void greaterInter(tree_n* curNode,variables* vars,tree_n* root);
void boolInter(tree_n* curNode,int* val1,int* val2,variables* vars,tree_n* root);
void boolIn1(tree_n* node,int* val,variables* vars,tree_n* root);
void boolIn2(tree_n* node,int* val,variables* vars,tree_n* root);
void boolIn3(char* literalValue,int* val);
void carInter(tree_n* curNode,variables* vars,tree_n* root);
void cdrInter(tree_n* curNode,variables* vars,tree_n* root);
void consInter(tree_n* curNode,variables* vars,tree_n* root);
void consIn0(tree_n* node,lisp** l,variables* vars,tree_n* root);
void consIn1(tree_n* node,lisp** l,variables* vars,tree_n* root);
void consIn2(tree_n* node,lisp** l,variables* vars,tree_n* root);
void printInter(tree_n* curNode,variables* vars,tree_n* root);
void printIn1(tree_n* node1,variables* vars,tree_n* root);
void printIn2(tree_n* node1,variables* vars,tree_n* root);
void setInter(tree_n* curNode,variables* vars,tree_n* root);
void setIn1(tree_n* node1,tree_n* node2,variables* vars,tree_n* root);
void setIn2(tree_n* node1,tree_n* node2,variables* vars,tree_n* root);
void setIn3(variable* oneVar,tree_n* node2,variables* vars,tree_n* root);
bool isVarExist(char* name,variables* vars);
int getVarIndex(char* name,variables* vars);
void falseFunc(tree_n* node,variables* vars,tree_n* root);
void freeTreeNodes(tree_n** curNode);
void freeTreeNodesHelp(tree_n* curNode);
void freeVariables(variables** vars);
void liAndStrModify(char* bef,char* str);
void varCheckHelp(int* index,tree_n* node,variables* vars,tree_n* root);
bool literalCheck(char* str);
bool literalCheckHelp1(char* str);
bool literalCheckHelp2(char* str);
void exitUnsuccess(variables* vars,tree_n* root);
void whiteTest();
