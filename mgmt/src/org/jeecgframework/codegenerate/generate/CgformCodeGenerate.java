/*     */ package org.jeecgframework.codegenerate.generate;
/*     */ 
/*     */ import freemarker.template.TemplateException;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.lang.StringUtils;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.jeecgframework.codegenerate.database.JeecgReadTable;
/*     */ import org.jeecgframework.codegenerate.pojo.CreateFileProperty;
/*     */ import org.jeecgframework.codegenerate.pojo.onetomany.SubTableEntity;
/*     */ import org.jeecgframework.codegenerate.util.CodeDateUtils;
/*     */ import org.jeecgframework.codegenerate.util.CodeResourceUtil;
/*     */ import org.jeecgframework.codegenerate.util.NonceUtils;
/*     */ import org.jeecgframework.codegenerate.util.def.FtlDef;
/*     */ import org.jeecgframework.web.cgform.entity.config.CgFormFieldEntity;
/*     */ import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
/*     */ import org.jeecgframework.web.cgform.entity.generate.GenerateEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CgformCodeGenerate
/*     */   implements ICallBack
/*     */ {
/*  36 */   private static final Log log = LogFactory.getLog(CgformCodeGenerate.class);
/*     */   
/*  38 */   private String entityPackage = "test";
/*  39 */   private String entityName = "Person";
/*  40 */   private String tableName = "person";
/*  41 */   private String ftlDescription = "公告";
/*  42 */   private String primaryKeyPolicy = "uuid";
/*  43 */   private String sequenceCode = "";
/*     */   private String[] foreignKeys;
/*  45 */   public static int FIELD_ROW_NUM = 1;
/*     */   
/*     */   private String cgformJspHtml;
/*     */   
/*     */   private SubTableEntity sub;
/*     */   private GenerateEntity subG;
/*     */   private CreateFileProperty subFileProperty;
/*     */   private String policy;
/*     */   private String[] array;
/*     */   private GenerateEntity cgformConfig;
/*  55 */   private static CreateFileProperty createFileProperty = new CreateFileProperty();
/*     */   
/*  57 */   static { createFileProperty.setActionFlag(true);
/*  58 */     createFileProperty.setServiceIFlag(true);
/*  59 */     createFileProperty.setJspFlag(true);
/*  60 */     createFileProperty.setServiceImplFlag(true);
/*  61 */     createFileProperty.setJspMode("01");
/*  62 */     createFileProperty.setPageFlag(true);
/*  63 */     createFileProperty.setEntityFlag(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CgformCodeGenerate() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public CgformCodeGenerate(CreateFileProperty createFileProperty2, GenerateEntity generateEntity)
/*     */   {
/*  74 */     this.entityName = generateEntity.getEntityName();
/*  75 */     this.entityPackage = generateEntity.getEntityPackage();
/*  76 */     this.tableName = generateEntity.getTableName();
/*  77 */     this.ftlDescription = generateEntity.getFtlDescription();
/*  78 */     FIELD_ROW_NUM = 1;
/*  79 */     createFileProperty = createFileProperty2;
/*  80 */     createFileProperty.setJspMode(createFileProperty2.getJspMode());
/*  81 */     this.primaryKeyPolicy = generateEntity.getPrimaryKeyPolicy();
/*  82 */     this.sequenceCode = "";
/*  83 */     this.cgformConfig = generateEntity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CgformCodeGenerate(SubTableEntity sub, GenerateEntity subG, CreateFileProperty subFileProperty, String policy, String[] array)
/*     */   {
/*  95 */     this.entityName = subG.getEntityName();
/*  96 */     this.entityPackage = subG.getEntityPackage();
/*  97 */     this.tableName = subG.getTableName();
/*  98 */     this.ftlDescription = subG.getFtlDescription();
/*  99 */     createFileProperty = subFileProperty;
/* 100 */     FIELD_ROW_NUM = 1;
/* 101 */     this.primaryKeyPolicy = policy;
/* 102 */     this.sequenceCode = "";
/* 103 */     this.cgformConfig = subG;
/* 104 */     this.foreignKeys = array;
/* 105 */     this.sub = sub;
/* 106 */     this.subG = subG;
/* 107 */     this.subFileProperty = subFileProperty;
/* 108 */     this.policy = policy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, Object> execute()
/*     */   {
/* 115 */     Map<String, Object> data = new HashMap();
/* 116 */     Map<String, String> fieldMeta = new HashMap();
/*     */     
/* 118 */     data.put("bussiPackage", CodeResourceUtil.bussiPackage);
/*     */     
/* 120 */     data.put("entityPackage", this.entityPackage);
/*     */     
/* 122 */     data.put("entityName", this.entityName);
/*     */     
/* 124 */     data.put("tableName", this.tableName);
/*     */     
/* 126 */     data.put("ftl_description", this.ftlDescription);
/*     */     
/* 128 */     data.put(FtlDef.JEECG_TABLE_ID, CodeResourceUtil.JEECG_GENERATE_TABLE_ID);
/*     */     
/* 130 */     data.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, this.primaryKeyPolicy);
/* 131 */     data.put(FtlDef.JEECG_SEQUENCE_CODE, this.sequenceCode);
/*     */     
/* 133 */     data.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));
/*     */     
/* 135 */     data.put("foreignKeys", this.foreignKeys);
/*     */     
/*     */ 
/* 138 */     data.put(FtlDef.FIELD_REQUIRED_NAME, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) : -1));
/*     */     
/* 140 */     data.put(FtlDef.SEARCH_FIELD_NUM, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) : -1));
/*     */     
/* 142 */     data.put(FtlDef.FIELD_ROW_NAME, Integer.valueOf(FIELD_ROW_NUM));
/*     */     try {
/* 144 */       List<CgFormFieldEntity> columns = this.cgformConfig.deepCopy().getCgFormHead().getColumns();
/*     */       String type;
/* 146 */       for (CgFormFieldEntity cf : columns) {
/* 147 */         type = cf.getType();
/* 148 */         if ("string".equalsIgnoreCase(type)) {
/* 149 */           cf.setType("java.lang.String");
/* 150 */         } else if ("Date".equalsIgnoreCase(type)) {
/* 151 */           cf.setType("java.util.Date");
/* 152 */         } else if ("double".equalsIgnoreCase(type)) {
/* 153 */           cf.setType("java.lang.Double");
/* 154 */         } else if ("int".equalsIgnoreCase(type)) {
/* 155 */           cf.setType("java.lang.Integer");
/* 156 */         } else if ("long".equalsIgnoreCase(type)) {
	/* 155 */           cf.setType("java.lang.Long");
	/* 156 */         } else if ("BigDecimal".equalsIgnoreCase(type)) {
/* 157 */           cf.setType("java.math.BigDecimal");
/* 158 */         } else if ("Text".equalsIgnoreCase(type)) {
/* 159 */           cf.setType("java.lang.String");
/* 160 */         } else if ("Blob".equalsIgnoreCase(type)) {
/* 161 */           cf.setType("java.sql.Blob");
/*     */         }
/* 163 */         String fieldName = cf.getFieldName();
/* 164 */         String fieldNameV = JeecgReadTable.formatField(fieldName);
/* 165 */         cf.setFieldName(fieldNameV);
/* 166 */         fieldMeta.put(fieldNameV, fieldName.toUpperCase());
/*     */       }
/* 168 */       List<CgFormFieldEntity> pageColumns = new ArrayList();
/* 169 */       for (CgFormFieldEntity cf : columns) {
/* 170 */         if ((StringUtils.isNotEmpty(cf.getIsShow())) && ("Y".equalsIgnoreCase(cf.getIsShow()))) {
/* 171 */           pageColumns.add(cf);
/*     */         }
/*     */       }
/*     */       
/* 175 */       data.put("cgformConfig", this.cgformConfig);
/* 176 */       data.put("fieldMeta", fieldMeta);
/* 177 */       data.put("columns", columns);
/* 178 */       data.put("pageColumns", pageColumns);
/* 179 */       data.put("buttons", this.cgformConfig.getButtons() == null ? new ArrayList(0) : this.cgformConfig.getButtons());
/* 180 */       data.put("buttonSqlMap", this.cgformConfig.getButtonSqlMap() == null ? new HashMap(0) : this.cgformConfig.getButtonSqlMap());
/* 181 */       data.put("packageStyle", this.cgformConfig.getPackageStyle());
/*     */     } catch (Exception e) {
/* 183 */       e.printStackTrace();
/*     */     }
/* 185 */     long serialVersionUID = NonceUtils.randomLong() + 
/* 186 */       NonceUtils.currentMills();
/* 187 */     data.put("serialVersionUID", String.valueOf(serialVersionUID));
/* 188 */     return data;
/*     */   }
/*     */   
/*     */   public void generateToFile() throws TemplateException, IOException {
/* 192 */     log.info("----jeecg---Code----Generation----[单表模型:" + this.tableName + "]------- 生成中。。。");
/*     */     
/* 194 */     CgformCodeFactory codeFactory = new CgformCodeFactory();
/* 195 */     codeFactory.setProjectPath(this.cgformConfig.getProjectPath());
/* 196 */     codeFactory.setPackageStyle(this.cgformConfig.getPackageStyle());
/* 197 */     if (this.cgformConfig.getCgFormHead().getJformType().intValue() == 1) {
/* 198 */       codeFactory.setCallBack(new CgformCodeGenerate(createFileProperty, this.cgformConfig));
/*     */     } else {
/* 200 */       codeFactory.setCallBack(new CgformCodeGenerate(this.sub, this.subG, this.subFileProperty, "uuid", this.foreignKeys));
/*     */     }
/*     */     
/* 203 */     if (createFileProperty.isJspFlag()) {
/* 204 */       if ("03".equals(createFileProperty.getJspMode())) {
/* 205 */         codeFactory.invoke("onetomany/cgform_jspSubTemplate.ftl", "jspList");
/*     */       }
/*     */       else {
/* 208 */         if (StringUtils.isNotEmpty(this.cgformJspHtml)) {
/* 209 */           codeFactory.invokeNotFlt(this.cgformJspHtml, "jsp_add");
/* 210 */           codeFactory.invokeNotFlt(this.cgformJspHtml.replace("doAdd", "doUpdate"), "jsp_update");
/*     */         }
/* 212 */         else if ("01".equals(createFileProperty.getJspMode())) {
/* 213 */           codeFactory.invoke("cgform_jspTableTemplate_add.ftl", "jsp_add");
/* 214 */           codeFactory.invoke("cgform_jspTableTemplate_update.ftl", "jsp_update");
/* 215 */         } else if ("02".equals(createFileProperty.getJspMode())) {
/* 216 */           codeFactory.invoke("cgform_jspDivTemplate_add.ftl", "jsp_add");
/* 217 */           codeFactory.invoke("cgform_jspDivTemplate_update.ftl", "jsp_update");
/*     */         }
/* 219 */         codeFactory.invoke("cgform_jspListTemplate.ftl", "jspList");
/* 220 */         codeFactory.invoke("cgform_jsListEnhanceTemplate.ftl", "jsList");
/* 221 */         codeFactory.invoke("cgform_jsEnhanceTemplate.ftl", "js");
/*     */       }
/*     */     }
/* 224 */     if (createFileProperty.isServiceImplFlag()) {
/* 225 */       codeFactory.invoke("cgform_serviceImplTemplate.ftl", "serviceImpl");
/*     */     }
/* 227 */     if (createFileProperty.isServiceIFlag()) {
/* 228 */       codeFactory.invoke("cgform_serviceITemplate.ftl", "service");
/*     */     }
/* 230 */     if (createFileProperty.isActionFlag()) {
/* 231 */       codeFactory.invoke("cgform_controllerTemplate.ftl", "controller");
/*     */     }
/* 233 */     if (createFileProperty.isEntityFlag())
/*     */     {
/* 235 */       codeFactory.invoke("cgform_entityTemplate.ftl", "entity");
/*     */     }
/* 237 */     log.info("----jeecg----Code----Generation-----[单表模型：" + this.tableName + "]------ 生成完成。。。");
/*     */   }
/*     */   
/*     */   public GenerateEntity getCgformConfig() {
/* 241 */     return this.cgformConfig;
/*     */   }
/*     */   
/*     */   public void setCgformConfig(GenerateEntity cgformConfig) {
/* 245 */     this.cgformConfig = cgformConfig;
/*     */   }
/*     */   
/* 248 */   public String getCgformJspHtml() { return this.cgformJspHtml; }
/*     */   
/*     */   public void setCgformJspHtml(String cgformJspHtml) {
/* 251 */     this.cgformJspHtml = cgformJspHtml;
/*     */   }
/*     */ }


/* Location:              C:\Users\Administrator\Desktop\tmp\codegenerate-3.6-20151203.154037-1.jar!\org\jeecgframework\codegenerate\generate\CgformCodeGenerate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */