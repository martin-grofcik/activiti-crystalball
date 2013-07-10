create table ACT_GE_PROPERTY (
    NAME_ varchar(64),
    VALUE_ varchar(300),
    REV_ integer,
    primary key (NAME_)
);

insert into ACT_GE_PROPERTY
values ('schema.version', '5.11', 1);

insert into ACT_GE_PROPERTY
values ('schema.history', 'create(5.11)', 1);

insert into ACT_GE_PROPERTY
values ('next.dbid', '1', 1);

create table ACT_GE_BYTEARRAY (
    ID_ varchar(64),
    REV_ integer,
    NAME_ varchar(255),
    DEPLOYMENT_ID_ varchar(64),
    BYTES_ longvarbinary,
    GENERATED_ bit,
    primary key (ID_)
);

create table ACT_RE_DEPLOYMENT (
    ID_ varchar(64),
    NAME_ varchar(255),
    CATEGORY_ varchar(255),
    DEPLOY_TIME_ timestamp,
    primary key (ID_)
);

create table ACT_RE_PROCDEF (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    CATEGORY_ varchar(255),
    NAME_ varchar(255),
    KEY_ varchar(255) NOT NULL,
    VERSION_ integer NOT NULL,
    DEPLOYMENT_ID_ varchar(64),
    RESOURCE_NAME_ varchar(4000),
    DGRM_RESOURCE_NAME_ varchar(4000),
    DESCRIPTION_ varchar(4000),
    HAS_START_FORM_KEY_ bit,
    SUSPENSION_STATE_ integer,
    primary key (ID_)
);

create table ACT_RE_MODEL (
    ID_ varchar(64) not null,
    REV_ integer,
    NAME_ varchar(255),
    KEY_ varchar(255),
    CATEGORY_ varchar(255),
    CREATE_TIME_ timestamp,
    LAST_UPDATE_TIME_ timestamp,
    VERSION_ integer,
    META_INFO_ varchar(4000),
    DEPLOYMENT_ID_ varchar(64),
    EDITOR_SOURCE_VALUE_ID_ varchar(64),
    EDITOR_SOURCE_EXTRA_VALUE_ID_ varchar(64),
    primary key (ID_)
);

create table CRB_RU_SIMULATION (
    ID_ varchar(64),
    NAME_ varchar(255),
    DESCRIPTION_ varchar(4000),
    AUTHOR_ varchar(255),
    START_ timestamp,
    END_ timestamp,
    SEED_ bigint,
    SIMULATION_CONFIG_URL_ varchar(255),
    REPLICATION_ integer,
    REPLICATION_COUNTER_ integer,
    SUSPENSION_STATE_ integer,
    primary key (ID_)
);

create table CRB_RU_SIMULATION_RUN (
    ID_ varchar(64),
    SIMULATION_ID_ varchar(64),
    SIMULATION_TIME_ timestamp,
    REPLICATION_ integer,
    SUSPENSION_STATE_ integer,
    primary key (ID_)
);


create table CRB_RU_JOB (
    ID_ varchar(64) NOT NULL,
    REV_ integer,
    LOCK_EXP_TIME_ timestamp,
    LOCK_OWNER_ varchar(255),
    EXCLUSIVE_ boolean,
    SIMULATION_INSTANCE_ID_ varchar(64),
    RETRIES_ integer,
    MESSAGE_ varchar(255),
    EXCEPTION_STACK_ID_ varchar(64),
    EXCEPTION_MSG_ varchar(4000),
    DUEDATE_ timestamp,
    REPEAT_ varchar(255),
    HANDLER_TYPE_ varchar(255),
    HANDLER_CFG_ varchar(4000),
    primary key (ID_)
);

create table CRB_RU_RESULT (
    ID_ varchar(64) NOT NULL,
    RUN_ID_ varchar(64),
    TYPE_ varchar(255),
    primary key (ID_)
);

create table CRB_RU_VARIABLE (
    ID_ varchar(64) not null,
    REV_ integer,
    TYPE_ varchar(255) not null,
    NAME_ varchar(255) not null,
    RUN_ID_ varchar(64),
    RESULT_ID_ varchar(64),
    SIMULATION_INST_ID_ varchar(64),
    BYTEARRAY_ID_ varchar(64),
    DOUBLE_ double,
    LONG_ bigint,
    TEXT_ varchar(4000),
    TEXT2_ varchar(4000),
    primary key (ID_)
);

create index CRB_IDX_SIM_BUSKEY on CRB_RU_SIMULATION(NAME_);

alter table ACT_GE_BYTEARRAY
    add constraint ACT_FK_BYTEARR_DEPL
    foreign key (DEPLOYMENT_ID_)
    references ACT_RE_DEPLOYMENT;

alter table ACT_RE_PROCDEF
    add constraint ACT_UNIQ_PROCDEF
    unique (KEY_,VERSION_);
    
alter table ACT_RE_MODEL 
    add constraint ACT_FK_MODEL_SOURCE 
    foreign key (EDITOR_SOURCE_VALUE_ID_) 
    references ACT_GE_BYTEARRAY (ID_);

alter table ACT_RE_MODEL 
    add constraint ACT_FK_MODEL_SOURCE_EXTRA 
    foreign key (EDITOR_SOURCE_EXTRA_VALUE_ID_) 
    references ACT_GE_BYTEARRAY (ID_);
    
alter table ACT_RE_MODEL 
    add constraint ACT_FK_MODEL_DEPLOYMENT 
    foreign key (DEPLOYMENT_ID_) 
    references ACT_RE_DEPLOYMENT (ID_);
    
alter table CRB_RU_JOB
    add constraint CRB_FK_JOB_EXCEPTION
    foreign key (EXCEPTION_STACK_ID_)
    references ACT_GE_BYTEARRAY;

alter table CRB_RU_SIMULATION_RUN
    add constraint CRB_FK_RUN_SIMULATION
    foreign key (SIMULATION_ID_)
    references CRB_RU_SIMULATION;

alter table CRB_RU_RESULT
    add constraint CRB_FK_RES_RUN
    foreign key (RUN_ID_)
    references CRB_RU_SIMULATION_RUN;

alter table CRB_RU_VARIABLE
    add constraint CRB_FK_VAR_SIM_RUN
    foreign key (RUN_ID_)
    references CRB_RU_SIMULATION_RUN;

alter table CRB_RU_VARIABLE
    add constraint CRB_FK_VAR_RESULT
    foreign key (RESULT_ID_)
    references CRB_RU_RESULT;

    
alter table CRB_RU_VARIABLE
    add constraint CRB_FK_VAR_SIMULATION_INSTANCE
    foreign key (SIMULATION_INST_ID_)
    references CRB_RU_SIMULATION;

alter table CRB_RU_VARIABLE
    add constraint CRB_FK_VAR_BYTEARRAY
    foreign key (BYTEARRAY_ID_)
    references ACT_GE_BYTEARRAY;