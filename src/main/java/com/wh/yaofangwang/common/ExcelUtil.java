package com.wh.yaofangwang.common;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.helper.StringUtil;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description excel 处理
 * @Author 002
 * @Date 2018/7/30 13:57
 **/
public class ExcelUtil {
    /**
     * 将excel返回
     * @param sheetName sheet的名称
     * @param list 要写入excel的数据
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, List<LinkedHashMap> list) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row0 = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        // 提取list 数据中列最多的作为表头
        if(list != null && list.size() > 0){
            //声明列对象
            HSSFCell cell = null;
            int columnSize = 0;// 有多少列
            LinkedHashMap<String,String> maxlengthData = null; // 列最多的一行数据
            for(int i=0 ; i<list.size();i++){
                LinkedHashMap<String,String> linkedHashMap = list.get(i);
                if(columnSize < linkedHashMap.size() ){
                    columnSize = linkedHashMap.size();
                    maxlengthData = linkedHashMap;
                }
            }
            String[] headRow = new String[maxlengthData.size()];// 用数组储存表头数据

            // 写表头
            int columnIndex = 0;
            for(Map.Entry<String, String> entry: maxlengthData.entrySet()){
                sheet.setColumnWidth(columnIndex,256*15);
                cell = row0.createCell(columnIndex);
                cell.setCellValue(entry.getKey());
                cell.setCellStyle(style);
                headRow[columnIndex] = entry.getKey();
                columnIndex++;
            }
            // 写入实际数据内容
            for(int i = 0; i< list.size(); i++){
                Map map = list.get(i);
                HSSFRow row = sheet.createRow(i + 1);//从第二行开始写数据
                for(int j = 0; j < headRow.length; j++){
                    String key = headRow[j];
                    cell = row.createCell(j);
                    cell.setCellValue(map.get(key) == null ?"" : map.get(key).toString());
                    cell.setCellStyle(style);
                }
//                for(Map.Entry entry: map.entrySet()){
//                    cell = row.createCell(columnIndex);
//                    cell.setCellValue(entry.getValue() == null ?"" : entry.getValue().toString());
//                    cell.setCellStyle(style);
//                    columnIndex++;
//                }
            }
        }else{
            LogUtil.info("ExcelUtil 数据为空");
        }
        return wb;
    }

    /**
     * 将excel返回
     * @param sheetName sheet的名称
     * @param list 要写入excel的数据
     * @return
     */
    private static HSSFWorkbook createHSSFWorkbook(HSSFWorkbook wb, String sheetName, List<LinkedHashMap> list) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
//        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row0 = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        // 提取list 数据中列最多的作为表头
        if(list != null && list.size() > 0){
            //声明列对象
            HSSFCell cell = null;
            int columnSize = 0;// 有多少列
            LinkedHashMap<String,String> maxlengthData = null; // 列最多的一行数据
            for(int i=0 ; i<list.size();i++){
                LinkedHashMap<String,String> linkedHashMap = list.get(i);
                if(columnSize < linkedHashMap.size() ){
                    columnSize = linkedHashMap.size();
                    maxlengthData = linkedHashMap;
                }
            }
            String[] headRow = new String[maxlengthData.size()];// 用数组储存表头数据

            // 写表头
            int columnIndex = 0;
            for(Map.Entry<String, String> entry: maxlengthData.entrySet()){
                sheet.setColumnWidth(columnIndex,256*15);
                cell = row0.createCell(columnIndex);
                cell.setCellValue(entry.getKey());
                cell.setCellStyle(style);
                headRow[columnIndex] = entry.getKey();
                columnIndex++;
            }
            // 写入实际数据内容
            for(int i = 0; i< list.size(); i++){
                Map map = list.get(i);
                HSSFRow row = sheet.createRow(i + 1);//从第二行开始写数据
                for(int j = 0; j < headRow.length; j++){
                    String key = headRow[j];
                    cell = row.createCell(j);
                    cell.setCellValue(map.get(key) == null ?"" : map.get(key).toString());
                    cell.setCellStyle(style);
                }
            }
        }else{
            LogUtil.info("ExcelUtil 数据为空");
        }
        return wb;
    }

    /*
     * @author wh
     * @desc   指定下载路径
     * @date   2018/8/29 11:52
     * @method createHSSFWorkbookSpecPath
     * @param  [sheetName, filePath, list]
     * @result void
     */
    public static  void createHSSFWorkbookSpecPath(String sheetName, String filePath,
                                                   String fileName, List<LinkedHashMap> list) {
        HSSFWorkbook wb = getHSSFWorkbook(sheetName,list);
        FileOutputStream fout = null;
        try {
            if (!StringUtils.isEmpty(filePath)){
                File file = new File(filePath);
                if(!file.exists()){
                    file.mkdir();
                }
            }
            File file = new File(filePath + fileName);
            fout = new FileOutputStream(file);
            wb.write(fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * @author wh
     * @desc   指定下载路径
     * @date   2018/8/29 11:52
     * @method createHSSFWorkbookSpecPath
     * @param  [sheetName, filePath, list]
     * @result void
     */
    public static  void createHSSFWorkbook(HSSFWorkbook wb, String sheetName, String filePath,
                                                   String fileName, List<LinkedHashMap> list) {
        wb = createHSSFWorkbook(wb, sheetName,list);
        FileOutputStream fout = null;
        try {
            if (!StringUtils.isEmpty(filePath)){
                File file = new File(filePath);
                if(!file.exists()){
                    file.mkdir();
                }
            }
            File file = new File(filePath + fileName);
            if (file.exists())
                file.delete();
            fout = new FileOutputStream(file);
            wb.write(fout);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * @Description 读取excel中的内容，组装成JSONArray
     * @Author zhongjianhui
     * @Date 2018/12/27 13:42
     * @param 文件路径（不带文件名）
     * @param 文件名
     * @return com.alibaba.fastjson.JSONArray
     **/
    public static JSONArray readExcel(String filePath, String fileName){
        JSONArray jsonArray = new JSONArray();
        if(!filePath.endsWith("/")){
            filePath = filePath + "/";
        }
        File file = new File(filePath+fileName);
        if(file.exists()){
            try {
                jsonArray = readExcel(fileName,new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            LogUtil.info("文件不存在");
        }
        return jsonArray;
    }


    /**
     * @Description 读取excel中的内容，组装成JSONArray
     * @Author zhongjianhui
     * @Date 2018/9/3 20:51
     * @param fileName excel 文件名称，如：test.xls
     * @param inputStream 文件输入流
     * @return com.alibaba.fastjson.JSONArray
     **/
    public static JSONArray readExcel(String fileName, InputStream inputStream) {
        JSONArray jsonArray = new JSONArray();
        if(StringUtils.isEmpty(fileName) ){
            LogUtil.info("fileName 为空");
            return jsonArray;
        }
        Workbook wb = null;
        try {
            if(ExcelImportUtils.isExcel2003(fileName)){
                wb = new HSSFWorkbook(inputStream);
            }else if(ExcelImportUtils.isExcel2007(fileName)){
                wb = new XSSFWorkbook(inputStream);
            }else{
                LogUtil.info("not excel format");
                return jsonArray;
            }
            //得到第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            //得到Excel的行数
            int totalRows = sheet.getPhysicalNumberOfRows();
            //总列数
            Row firstRow = null;
            int totalCells = 0;
            //得到Excel的列数
            if(totalRows > 0){
                firstRow = sheet.getRow(0); // 第一行，表头
                if(null == firstRow){
                    LogUtil.info("表头为null");
                    return jsonArray;
                }
                totalCells = firstRow.getPhysicalNumberOfCells();
            }else{
                LogUtil.info("totalRows < 1");
                return jsonArray;
            }
            LogUtil.info("totalRows:"+totalRows+", totalCells:"+totalCells);
            JSONObject jsonObject = null;
            for(int r =1; r <=totalRows-1; r++){
                Row row = sheet.getRow(r);
                if(null == row){
                    LogUtil.info("第"+row+"行为null");
                }else{
                    jsonObject = new JSONObject(true);
                    for(int c=0; c<=totalCells-1; c++){
                        Cell cell = row.getCell(c);
                        if (null != cell){
                            String columnName = firstRow.getCell(c).getStringCellValue();
                            switch (cell.getCellTypeEnum()) {
                                case STRING: // 字符串
                                    jsonObject.put(columnName,cell.getStringCellValue()==null?"":cell.getStringCellValue());
                                    break;
                                case NUMERIC: // 数字
                                    jsonObject.put(columnName,cell.getNumericCellValue());
                                    break;
                                case FORMULA: // Boolean
                                    jsonObject.put(columnName,cell.getCellFormula());
                                    break;
                                case BOOLEAN: // 公式
                                    jsonObject.put(columnName,cell.getBooleanCellValue());
                                    break;
                                default:
                                    jsonObject.put(columnName,"");
                                    break;
                            }
                        }
                    }
                    jsonArray.add(jsonObject);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (wb != null){
                    wb.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    /*
     * @Description 下载文件
     * @Author zhongjianhui
     * @Date 2019/1/7 11:47
     * @param fileDir
     * @param filename
     * @param response
     * @return void
     **/
    public static void downloadFile(String fileDir, String filename, HttpServletResponse response) throws Exception{
        if(!fileDir.endsWith("/")){
            fileDir = fileDir + "/";
        }
        DataInputStream in = null;
        ServletOutputStream out = null;
        try {
            File file = new File(fileDir + filename);
            if (file.exists()) {
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(),"ISO8859-1"));
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
                in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                out = response.getOutputStream();
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
            }else{
                LogUtil.info("downloadFile, 文件不存在，"+fileDir + filename);
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                response.getWriter().print("文件不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

