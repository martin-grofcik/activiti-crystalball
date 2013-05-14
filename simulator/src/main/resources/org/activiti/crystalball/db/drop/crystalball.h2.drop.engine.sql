drop index ACT_IDX_EXEC_BUSKEY;

alter table ACT_GE_BYTEARRAY 
    drop constraint ACT_FK_BYTEARR_DEPL;

alter table ACT_RE_PROCDEF
    drop constraint ACT_UNIQ_PROCDEF;
    
alter table ACT_RE_MODEL 
    drop constraint ACT_FK_MODEL_SOURCE; 

alter table ACT_RE_MODEL 
    drop constraint ACT_FK_MODEL_SOURCE_EXTRA; 
    
alter table ACT_RE_MODEL 
    drop constraint ACT_FK_MODEL_DEPLOYMENT;

alter table CRB_RU_JOB
	drop constraint CRB_FK_JOB_EXCEPTION;

alter table CRB_RU_SIMULATION_RUN
	drop constraint CRB_FK_RUN_SIMULATION;

alter table CRB_RU_RESULT
	drop constraint CRB_FK_RES_RUN;
	
alter table CRB_RU_VARIABLE
    drop constraint CRB_FK_VAR_EXE;

alter table CRB_RU_VARIABLE
    drop constraint CRB_FK_VAR_SIMULATION_INSTANCE;

alter table CRB_RU_VARIABLE
    drop constraint CRB_FK_VAR_BYTEARRAY;

drop table ACT_GE_PROPERTY;

drop table ACT_GE_BYTEARRAY;

drop table ACT_RE_DEPLOYMENT;

drop table ACT_RE_PROCDEF;

drop table ACT_RE_MODEL;

drop table CRB_RU_SIMULATION;

drop table CRB_RU_SIMULATION_RUN;

drop table CRB_RU_JOB;

drop table CRB_RU_RESULT;

drop table CRB_RU_VARIABLE;