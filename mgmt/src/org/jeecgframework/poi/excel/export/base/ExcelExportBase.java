/**
 * Copyright 2013-2015 JueYue (qrb.jueyue@gmail.com)
 *   
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jeecgframework.poi.excel.export.base;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.jeecgframework.poi.excel.entity.params.MergeEntity;
import org.jeecgframework.poi.excel.entity.vo.PoiBaseConstants;
import org.jeecgframework.poi.excel.export.styler.IExcelExportStyler;
import org.jeecgframework.poi.util.PoiMergeCellUtil;
import org.jeecgframework.poi.util.PoiPublicUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供POI基础操作服务
 * 
 * @author JueYue
 * @date 2014年6月17日 下午6:15:13
 */
public abstract class ExcelExportBase extends ExportBase {

    private static final Logger        LOGGER        = LoggerFactory
                                                         .getLogger(ExcelExportBase.class);

    private int                        currentIndex  = 0;

    protected ExcelType                type          = ExcelType.XSSF;

    private Map<Integer, Double>       statistics    = new HashMap<Integer, Double>();

    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    private IExcelExportStyler         excelExportStyler;

    /**
     * 创建 最主要的 Cells
     * 
     * @param styles
     * @param rowHeight
     * @throws Exception
     */
    public int createCells(Drawing patriarch, int index, Object t,
                           List<ExcelExportEntity> excelParams, Sheet sheet, Workbook workbook,
                           short rowHeight) throws Exception {
        ExcelExportEntity entity;
        Row row = sheet.createRow(index);
        row.setHeight(rowHeight);
        int maxHeight = 1, cellNum = 0;
        int indexKey = createIndexCell(row, index, excelParams.get(0));
        cellNum += indexKey;
        for (int k = indexKey, paramSize = excelParams.size(); k < paramSize; k++) {
            entity = excelParams.get(k);
            if (entity.getList() != null) {
                Collection<?> list = getListCellValue(entity, t);
                int listC = 0;
                for (Object obj : list) {
                    createListCells(patriarch, index + listC, cellNum, obj, entity.getList(),
                        sheet, workbook);
                    listC++;
                }
                cellNum += entity.getList().size();
                if (list != null && list.size() > maxHeight) {
                    maxHeight = list.size();
                }
            } else {
                Object value = getCellValue(entity, t);
                if (entity.getType() == 1) {
                    createStringCell(row, cellNum++, value == null ? "" : value.toString(),
                        index % 2 == 0 ? getStyles(false, entity) : getStyles(true, entity), entity);
                } else {
                    createImageCell(patriarch, entity, row, cellNum++,
                        value == null ? "" : value.toString(), t);
                }
            }
        }
        // 合并需要合并的单元格
        cellNum = 0;
        for (int k = indexKey, paramSize = excelParams.size(); k < paramSize; k++) {
            entity = excelParams.get(k);
            if (entity.getList() != null) {
                cellNum += entity.getList().size();
            } else if (entity.isNeedMerge()) {
                for (int i = index + 1; i < index + maxHeight; i++) {
                    sheet.getRow(i).createCell(cellNum);
                    sheet.getRow(i).getCell(cellNum).setCellStyle(getStyles(false, entity));
                }
                sheet.addMergedRegion(new CellRangeAddress(index, index + maxHeight - 1, cellNum,
                    cellNum));
                cellNum++;
            }
        }
        return maxHeight;

    }

    /**
     * 图片类型的Cell
     * 
     * @param patriarch
     * @param entity
     * @param row
     * @param i
     * @param imagePath
     * @param obj
     * @throws Exception
     */
    public void createImageCell(Drawing patriarch, ExcelExportEntity entity, Row row, int i,
                                String imagePath, Object obj) throws Exception {
        row.setHeight((short) (50 * entity.getHeight()));
        row.createCell(i);
        ClientAnchor anchor;
        if (type.equals(ExcelType.HSSF)) {
            anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) i, row.getRowNum(), (short) (i + 1),
                row.getRowNum() + 1);
        } else {
            anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) i, row.getRowNum(), (short) (i + 1),
                row.getRowNum() + 1);
        }

        if (StringUtils.isEmpty(imagePath)) {
            return;
        }
        if (entity.getExportImageType() == 1) {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            BufferedImage bufferImg;
            try {
                String path = PoiPublicUtil.getWebRootPath(imagePath);
                path = path.replace("WEB-INF/classes/", "");
                path = path.replace("file:/", "");
                bufferImg = ImageIO.read(new File(path));
                ImageIO.write(bufferImg,
                    imagePath.substring(imagePath.indexOf(".") + 1, imagePath.length()),
                    byteArrayOut);
                byte[] value = byteArrayOut.toByteArray();
                patriarch.createPicture(anchor,
                    row.getSheet().getWorkbook().addPicture(value, getImageType(value)));
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            byte[] value = (byte[]) (entity.getMethods() != null ? getFieldBySomeMethod(
                entity.getMethods(), obj) : entity.getMethod().invoke(obj, new Object[] {}));
            if (value != null) {
                patriarch.createPicture(anchor,
                    row.getSheet().getWorkbook().addPicture(value, getImageType(value)));
            }
        }

    }

    private int createIndexCell(Row row, int index, ExcelExportEntity excelExportEntity) {
        if (excelExportEntity.getName().equals("序号")
            && excelExportEntity.getFormat().equals(PoiBaseConstants.IS_ADD_INDEX)) {
            createStringCell(row, 0, currentIndex + "", index % 2 == 0 ? getStyles(false, null)
                : getStyles(true, null), null);
            currentIndex = currentIndex + 1;
            return 1;
        }
        return 0;
    }

    /**
     * 创建List之后的各个Cells
     * 
     * @param styles
     */
    public void createListCells(Drawing patriarch, int index, int cellNum, Object obj,
                                List<ExcelExportEntity> excelParams, Sheet sheet, Workbook workbook)
                                                                                                    throws Exception {
        ExcelExportEntity entity;
        Row row;
        if (sheet.getRow(index) == null) {
            row = sheet.createRow(index);
            row.setHeight(getRowHeight(excelParams));
        } else {
            row = sheet.getRow(index);
        }
        for (int k = 0, paramSize = excelParams.size(); k < paramSize; k++) {
            entity = excelParams.get(k);
            Object value = getCellValue(entity, obj);
            if (entity.getType() == 1) {
                createStringCell(row, cellNum++, value == null ? "" : value.toString(),
                    row.getRowNum() % 2 == 0 ? getStyles(false, entity) : getStyles(true, entity),
                    entity);
            } else {
                createImageCell(patriarch, entity, row, cellNum++,
                    value == null ? "" : value.toString(), obj);
            }
        }
    }

    /**
     * 创建文本类型的Cell
     * 
     * @param row
     * @param index
     * @param text
     * @param style
     * @param entity
     */
    public void createStringCell(Row row, int index, String text, CellStyle style,
                                 ExcelExportEntity entity) {
        Cell cell = row.createCell(index);
        if (style != null && style.getDataFormat() > 0 && style.getDataFormat() < 12) {
            cell.setCellValue(Double.parseDouble(text));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else {
            RichTextString Rtext;
            if (type.equals(ExcelType.HSSF)) {
                Rtext = new HSSFRichTextString(text);
            } else {
                Rtext = new XSSFRichTextString(text);
            }
            cell.setCellValue(Rtext);
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
        addStatisticsData(index, text, entity);
    }

    /**
     * 创建统计行
     * @param styles 
     * @param sheet
     */
    public void addStatisticsRow(CellStyle styles, Sheet sheet) {
        if (statistics.size() > 0) {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            createStringCell(row, 0, "合计", styles, null);
            for (Integer key : keys) {
                createStringCell(row, key, DOUBLE_FORMAT.format(statistics.get(key)), styles, null);
            }
            statistics.clear();
        }

    }

    /**
     * 合计统计信息
     * @param index
     * @param text
     * @param entity
     */
    private void addStatisticsData(Integer index, String text, ExcelExportEntity entity) {
        if (entity != null && entity.isStatistics()) {
            Double temp = 0D;
            if (!statistics.containsKey(index)) {
                statistics.put(index, temp);
            }
            try {
                temp = Double.valueOf(text);
            } catch (NumberFormatException e) {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }

    /**
     * 获取导出报表的字段总长度
     * 
     * @param excelParams
     * @return
     */
    public int getFieldWidth(List<ExcelExportEntity> excelParams) {
        int length = -1;// 从0开始计算单元格的
        for (ExcelExportEntity entity : excelParams) {
            length += entity.getList() != null ? entity.getList().size() : 1;
        }
        return length;
    }

    /**
     * 获取图片类型,设置图片插入类型
     * 
     * @param value
     * @return
     * @Author JueYue
     * @date 2013年11月25日
     */
    public int getImageType(byte[] value) {
        String type = PoiPublicUtil.getFileExtendName(value);
        if (type.equalsIgnoreCase("JPG")) {
            return Workbook.PICTURE_TYPE_JPEG;
        } else if (type.equalsIgnoreCase("PNG")) {
            return Workbook.PICTURE_TYPE_PNG;
        }
        return Workbook.PICTURE_TYPE_JPEG;
    }

    private Map<Integer, int[]> getMergeDataMap(List<ExcelExportEntity> excelParams) {
        Map<Integer, int[]> mergeMap = new HashMap<Integer, int[]>();
        // 设置参数顺序,为之后合并单元格做准备
        int i = 0;
        for (ExcelExportEntity entity : excelParams) {
            if (entity.isMergeVertical()) {
                mergeMap.put(i, entity.getMergeRely());
            }
            if (entity.getList() != null) {
                for (ExcelExportEntity inner : entity.getList()) {
                    if (inner.isMergeVertical()) {
                        mergeMap.put(i, inner.getMergeRely());
                    }
                    i++;
                }
            } else {
                i++;
            }
        }
        return mergeMap;
    }

    /**
     * 获取样式
     * @param entity 
     * @param needOne
     * @return
     */
    public CellStyle getStyles(boolean needOne, ExcelExportEntity entity) {
        return excelExportStyler.getStyles(needOne, entity);
    }

    /**
     * 合并单元格
     * 
     * @param sheet
     * @param excelParams
     * @param styles 
     */
    public void mergeCells(Sheet sheet, List<ExcelExportEntity> excelParams, int titleHeight) {
        Map<Integer, int[]> mergeMap = getMergeDataMap(excelParams);
        PoiMergeCellUtil.mergeCells(sheet, mergeMap, titleHeight);
    }

    public void setCellWith(List<ExcelExportEntity> excelParams, Sheet sheet) {
        int index = 0;
        for (int i = 0; i < excelParams.size(); i++) {
            if (excelParams.get(i).getList() != null) {
                List<ExcelExportEntity> list = excelParams.get(i).getList();
                for (int j = 0; j < list.size(); j++) {
                    sheet.setColumnWidth(index, (int) (256 * list.get(j).getWidth()));
                    index++;
                }
            } else {
                sheet.setColumnWidth(index, (int) (256 * excelParams.get(i).getWidth()));
                index++;
            }
        }
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setExcelExportStyler(IExcelExportStyler excelExportStyler) {
        this.excelExportStyler = excelExportStyler;
    }

    public IExcelExportStyler getExcelExportStyler() {
        return excelExportStyler;
    }

}
