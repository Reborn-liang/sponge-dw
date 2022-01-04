/*     */ package org.jeecgframework.codegenerate.generate.onetomany;
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
/*     */ import org.jeecgframework.codegenerate.generate.CgformCodeGenerate;
/*     */ import org.jeecgframework.codegenerate.generate.ICallBack;
/*     */ import org.jeecgframework.codegenerate.pojo.CreateFileProperty;
/*     */ import org.jeecgframework.codegenerate.pojo.onetomany.CodeParamEntity;
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
/*     */ public class CgformCodeGenerateOneToMany
/*     */   implements ICallBack
/*     */ {
/*  39 */   private static final Log log = LogFactory.getLog(CgformCodeGenerateOneToMany.class);
/*     */   
/*  41 */   private String entityPackage = "test";
/*  42 */   private String entityName = "Person";
/*  43 */   private String tableName = "person";
/*  44 */   private String ftlDescription = "用户";
/*  45 */   private String primaryKeyPolicy = "uuid";
/*  46 */   private String sequenceCode = "";
/*     */   
/*     */ 
/*     */ 
/*     */   private static String ftl_mode;
/*     */   
/*     */ 
/*  53 */   public static String FTL_MODE_A = "A";
/*  54 */   public static String FTL_MODE_B = "B";
/*     */   
/*  56 */   private static List<SubTableEntity> subTabParam = new ArrayList();
/*  57 */   private static CreateFileProperty createFileProperty = new CreateFileProperty();
/*  58 */   public static int FIELD_ROW_NUM = 4;
/*     */   
/*     */ 
/*  61 */   private List<SubTableEntity> subTabFtl = new ArrayList();
/*     */   
/*  63 */   static { createFileProperty.setActionFlag(true);
/*  64 */     createFileProperty.setServiceIFlag(true);
/*  65 */     createFileProperty.setJspFlag(true);
/*  66 */     createFileProperty.setServiceImplFlag(true);
/*  67 */     createFileProperty.setPageFlag(true);
/*  68 */     createFileProperty.setEntityFlag(true);
/*     */   }
/*     */   
/*     */ 
/*     */   private CodeParamEntity codeParamEntityIn;
/*     */   
/*     */   private GenerateEntity mainG;
/*     */   
/*     */   private Map<String, GenerateEntity> subsG;
/*     */   
/*     */   private List<SubTableEntity> subTabParamIn;
/*     */   
/*     */   public CgformCodeGenerateOneToMany() {}
/*     */   
/*     */ 
/*     */   public CgformCodeGenerateOneToMany(List<SubTableEntity> subTabParamIn, CodeParamEntity codeParamEntityIn, GenerateEntity mainG, Map<String, GenerateEntity> subsG)
/*     */   {
/*  85 */     this.entityName = codeParamEntityIn.getEntityName();
/*  86 */     this.entityPackage = codeParamEntityIn.getEntityPackage();
/*  87 */     this.tableName = codeParamEntityIn.getTableName();
/*  88 */     this.ftlDescription = codeParamEntityIn.getFtlDescription();
/*  89 */     subTabParam = codeParamEntityIn.getSubTabParam();
/*  90 */     ftl_mode = codeParamEntityIn.getFtl_mode();
/*  91 */     this.primaryKeyPolicy = "uuid";
/*  92 */     this.sequenceCode = codeParamEntityIn.getSequenceCode();
/*  93 */     this.subTabParamIn = subTabParamIn;
/*  94 */     this.mainG = mainG;
/*  95 */     this.subsG = subsG;
/*  96 */     this.codeParamEntityIn = codeParamEntityIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> execute()
/*     */   {
/* 105 */     Map<String, Object> data = new HashMap();
/*     */     
/*     */ 
/* 108 */     data.put("bussiPackage", CodeResourceUtil.bussiPackage);
/*     */     
/* 110 */     data.put("entityPackage", this.entityPackage);
/*     */     
/* 112 */     data.put("entityName", this.entityName);
/*     */     
/* 114 */     data.put("tableName", this.tableName);
/*     */     
/* 116 */     data.put("ftl_description", this.ftlDescription);
/*     */     
/* 118 */     data.put("jeecg_table_id", CodeResourceUtil.JEECG_GENERATE_TABLE_ID);
/*     */     
/* 120 */     data.put(FtlDef.JEECG_PRIMARY_KEY_POLICY, this.primaryKeyPolicy);
/* 121 */     data.put(FtlDef.JEECG_SEQUENCE_CODE, this.sequenceCode);
/* 122 */     data.put("ftl_create_time", CodeDateUtils.dateToString(new Date()));
/*     */     
/*     */ 
/* 125 */     data.put(FtlDef.FIELD_REQUIRED_NAME, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_REQUIRED_NUM) : -1));
/*     */     
/* 127 */     data.put(FtlDef.SEARCH_FIELD_NUM, Integer.valueOf(StringUtils.isNotEmpty(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) ? Integer.parseInt(CodeResourceUtil.JEECG_UI_FIELD_SEARCH_NUM) : -1));
/*     */     
/* 129 */     data.put(FtlDef.FIELD_ROW_NAME, Integer.valueOf(FIELD_ROW_NUM));
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 134 */       Map<String, String> fieldMeta = new HashMap();
/* 135 */       List<CgFormFieldEntity> columns = this.mainG.deepCopy().getCgFormHead().getColumns();
/* 137 */       for (CgFormFieldEntity cf : columns) {
/* 138 */         String type = cf.getType();
/* 139 */         if ("string".equalsIgnoreCase(type)) {
/* 140 */           cf.setType("java.lang.String");
/* 141 */         } else if ("Date".equalsIgnoreCase(type)) {
/* 142 */           cf.setType("java.util.Date");
/* 143 */         } else if ("double".equalsIgnoreCase(type)) {
/* 144 */           cf.setType("java.lang.Double");
/* 145 */         } else if ("long".equalsIgnoreCase(type)) {
	/* 146 */           cf.setType("java.lang.Long");
	/* 147 */         } else if ("int".equalsIgnoreCase(type)) {
/* 146 */           cf.setType("java.lang.Integer");
/* 147 */         } else if ("BigDecimal".equalsIgnoreCase(type)) {
/* 148 */           cf.setType("java.math.BigDecimal");
/* 149 */         } else if ("Text".equalsIgnoreCase(type)) {
/* 150 */           cf.setType("javax.xml.soap.Text");
/* 151 */         } else if ("Blob".equalsIgnoreCase(type)) {
/* 152 */           cf.setType("java.sql.Blob");
/*     */         }
/* 154 */         String fieldName = cf.getFieldName();
/* 155 */         String fieldNameV = JeecgReadTable.formatField(fieldName);
/* 156 */         cf.setFieldName(fieldNameV);
/* 157 */         fieldMeta.put(fieldNameV, fieldName.toUpperCase());
/*     */       }
/* 159 */       List<CgFormFieldEntity> pageColumns = new ArrayList();
/* 160 */       for (CgFormFieldEntity cf : columns) {
/* 161 */         if ((StringUtils.isNotEmpty(cf.getIsShow())) && ("Y".equalsIgnoreCase(cf.getIsShow()))) {
/* 162 */           pageColumns.add(cf);
/*     */         }
/*     */       }
/* 165 */       String[] subtables = this.mainG.getCgFormHead().getSubTableStr().split(",");
/*     */       
/* 167 */       data.put("cgformConfig", this.mainG);
/* 168 */       data.put("fieldMeta", fieldMeta);
/* 169 */       data.put("columns", columns);
/* 170 */       data.put("pageColumns", pageColumns);
/* 171 */       data.put("buttons", this.mainG.getButtons() == null ? new ArrayList(0) : this.mainG.getButtons());
/* 172 */       data.put("buttonSqlMap", this.mainG.getButtonSqlMap() == null ? new HashMap(0) : this.mainG.getButtonSqlMap());
/* 173 */       data.put("subtables", subtables);
/* 174 */       data.put("subTab", this.subTabParamIn);
/*     */       
/*     */ 
/* 177 */       Map<String, List<CgFormFieldEntity>> subColumnsMap = new HashMap(0);
/* 178 */       Map<String, List<CgFormFieldEntity>> subPageColumnsMap = new HashMap(0);
/* 179 */       Map<String, String> subFieldMeta = new HashMap(0);
/* 180 */       Map<String, String> subFieldMeta1 = new HashMap(0);
/* 181 */       for (String key : this.subsG.keySet()) {
/* 182 */         GenerateEntity subG = (GenerateEntity)this.subsG.get(key);
/* 183 */         List<CgFormFieldEntity> subColumns = subG.deepCopy().getCgFormHead().getColumns();
/* 184 */         List<CgFormFieldEntity> subPageColumns = new ArrayList();
/* 185 */         for (CgFormFieldEntity cf : subColumns) {
/* 186 */           String type = cf.getType();
/* 187 */           if ("string".equalsIgnoreCase(type)) {
/* 188 */             cf.setType("java.lang.String");
/* 189 */           } else if ("Date".equalsIgnoreCase(type)) {
/* 190 */             cf.setType("java.util.Date");
/* 191 */           } else if ("double".equalsIgnoreCase(type)) {
/* 192 */             cf.setType("java.lang.Double");
/* 193 */           } else if ("long".equalsIgnoreCase(type)) {
	/* 194 */             cf.setType("java.lang.Long");
	/* 195 */           } else if ("int".equalsIgnoreCase(type)) {
/* 194 */             cf.setType("java.lang.Integer");
/* 195 */           } else if ("BigDecimal".equalsIgnoreCase(type)) {
/* 196 */             cf.setType("java.math.BigDecimal");
/* 197 */           } else if ("Text".equalsIgnoreCase(type)) {
/* 198 */             cf.setType("javax.xml.soap.Text");
/* 199 */           } else if ("Blob".equalsIgnoreCase(type)) {
/* 200 */             cf.setType("java.sql.Blob");
/*     */           }
/* 202 */           String fieldName = cf.getFieldName();
/* 203 */           String fieldNameV = JeecgReadTable.formatField(fieldName);
/* 204 */           cf.setFieldName(fieldNameV);
/* 205 */           subFieldMeta.put(fieldNameV, fieldName.toUpperCase());
/* 206 */           subFieldMeta1.put(fieldName.toUpperCase(), fieldNameV);
/* 207 */           if ((StringUtils.isNotEmpty(cf.getIsShow())) && ("Y".equalsIgnoreCase(cf.getIsShow()))) {
/* 208 */             subPageColumns.add(cf);
/*     */           }
/* 210 */           String mtable = cf.getMainTable();
/* 211 */           String mfiled = cf.getMainField();
/* 212 */           if ((mtable != null) && (mtable.equalsIgnoreCase(this.mainG.getTableName()))) {
/* 213 */             data.put(key + "_fk", mfiled);
/*     */           }
/* 215 */           subColumnsMap.put(key, subColumns);
/* 216 */           subPageColumnsMap.put(key, subPageColumns);
/*     */         }
/* 218 */         data.put("subColumnsMap", subColumnsMap);
/* 219 */         data.put("subPageColumnsMap", subPageColumnsMap);
/* 220 */         data.put("subFieldMeta", subFieldMeta);
/* 221 */         data.put("subFieldMeta1", subFieldMeta1);
/* 222 */         data.put("packageStyle", this.mainG.getPackageStyle());
/*     */       }
/*     */     } catch (Exception e) {
/* 225 */       e.printStackTrace();
/*     */     }
/* 227 */     long serialVersionUID = NonceUtils.randomLong() + 
/* 228 */       NonceUtils.currentMills();
/* 229 */     data.put("serialVersionUID", String.valueOf(serialVersionUID));
/* 230 */     return data;
/*     */   }
/*     */   
/*     */   public void generateToFile() throws TemplateException, IOException {
/* 234 */     CgformCodeFactoryOneToMany codeFactoryOneToMany = new CgformCodeFactoryOneToMany();
/* 235 */     codeFactoryOneToMany.setProjectPath(this.mainG.getProjectPath());
/* 236 */     codeFactoryOneToMany.setPackageStyle(this.mainG.getPackageStyle());
/* 237 */     codeFactoryOneToMany.setCallBack(new CgformCodeGenerateOneToMany(this.subTabParamIn, this.codeParamEntityIn, this.mainG, this.subsG));
/*     */     
/* 239 */     if (createFileProperty.isJspFlag()) {
/* 240 */       codeFactoryOneToMany.invoke("onetomany/cgform_jspListTemplate.ftl", "jspList");
/* 241 */       codeFactoryOneToMany.invoke("onetomany/cgform_jspTemplate_add.ftl", "jsp_add");
/* 242 */       codeFactoryOneToMany.invoke("onetomany/cgform_jspTemplate_update.ftl", "jsp_update");
/* 243 */       codeFactoryOneToMany.invoke("onetomany/cgform_jsEnhanceTemplate.ftl", "js");
/* 244 */       codeFactoryOneToMany.invoke("onetomany/cgform_jsListEnhanceTemplate.ftl", "jsList");
/*     */     }
/* 246 */     if (createFileProperty.isServiceImplFlag()) {
/* 247 */       codeFactoryOneToMany.invoke("onetomany/cgform_serviceImplTemplate.ftl", "serviceImpl");
/*     */     }
/* 249 */     if (createFileProperty.isServiceIFlag()) {
/* 250 */       codeFactoryOneToMany.invoke("onetomany/cgform_serviceITemplate.ftl", "service");
/*     */     }
/* 252 */     if (createFileProperty.isActionFlag()) {
/* 253 */       codeFactoryOneToMany.invoke("onetomany/cgform_controllerTemplate.ftl", "controller");
/*     */     }
/* 255 */     if (createFileProperty.isEntityFlag())
/*     */     {
/* 257 */       codeFactoryOneToMany.invoke("onetomany/cgform_entityTemplate.ftl", "entity");
/*     */     }
/* 259 */     if (createFileProperty.isPageFlag())
/*     */     {
/* 261 */       codeFactoryOneToMany.invoke("onetomany/cgform_pageTemplate.ftl", "page");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void oneToManyCreate(List<SubTableEntity> subTabParamIn, CodeParamEntity codeParamEntityIn, GenerateEntity mainG, Map<String, GenerateEntity> subsG)
/*     */     throws TemplateException, IOException
/*     */   {
/* 273 */     log.info("----jeecg----Code-----Generation-----[一对多数据模型：" + codeParamEntityIn.getTableName() + "]------- 生成中。。。");
/*     */     
/* 275 */     CreateFileProperty subFileProperty = new CreateFileProperty();
/* 276 */     subFileProperty.setActionFlag(false);
/* 277 */     subFileProperty.setServiceIFlag(false);
/* 278 */     subFileProperty.setJspFlag(true);
/* 279 */     subFileProperty.setServiceImplFlag(false);
/* 280 */     subFileProperty.setPageFlag(false);
/* 281 */     subFileProperty.setEntityFlag(true);
/* 282 */     subFileProperty.setJspMode("03");
/*     */     
/* 284 */     for (SubTableEntity sub : subTabParamIn) {
/* 285 */       String[] foreignKeys = sub.getForeignKeys();
/* 286 */       GenerateEntity subG = (GenerateEntity)subsG.get(sub.getTableName());
/* 287 */       new CgformCodeGenerate(sub, subG, subFileProperty, "uuid", foreignKeys).generateToFile();
/*     */     }
/*     */     
/*     */ 
/* 291 */     new CgformCodeGenerateOneToMany(subTabParamIn, codeParamEntityIn, mainG, subsG).generateToFile();
/* 292 */     log.info("----jeecg----Code----Generation------[一对多数据模型：" + codeParamEntityIn.getTableName() + "]------ 生成完成。。。");
/*     */   }
/*     */ }


/* Location:              C:\Users\Administrator\Desktop\tmp\codegenerate-3.6-20151203.154037-1.jar!\org\jeecgframework\codegenerate\generate\onetomany\CgformCodeGenerateOneToMany.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */