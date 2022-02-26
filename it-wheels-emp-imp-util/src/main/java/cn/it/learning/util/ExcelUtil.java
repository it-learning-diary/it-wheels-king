package cn.it.learning.util;

/**
 * @Author it-learning
 * @Description excel工具包
 * @Date 2022/2/25 18:05
 * @Version 1.0
 */
@Slf4j
@Component
public class ExcelUtil<T> {


    /**
     * excel文件导出(可以包含多个sheet页)
     *
     * @param fileName   导出文件名
     * @param head       导出表头(多个sheet页就是多个集合元素)
     * @param exportData 导出数据
     * @param sheetNames 导出页签名
     */
    public void exportFile(HttpServletResponse response, String fileName, List<T> head, List<List<T>> exportData, List<String> sheetNames) {
        if (Objects.isNull(response) || StrUtil.isBlank(fileName) || CollUtil.isEmpty(head)) {
            log.info("ExcelUtil exportFile param can't be empty");
            return;
        }
        ExcelWriter writer = null;
        try {
            response.setContentType(Constant.EXCEL_CONTENT_TYPE);
            response.setCharacterEncoding(Constant.UTF_8);
            response.setHeader(Constant.CONTENT_DISPOSITION, Constant.ATTACHMENT_FILENAME + fileName + Constant.XLSX_SUFFIX);
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 获取默认样式的excel表格对象
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = getExportDefaultStyle();
            writer = EasyExcel.write(response.getOutputStream()).registerWriteHandler(horizontalCellStyleStrategy).build();
            for (int i = 0; i < exportData.size(); i++) {
                // 获取表头数据
                Object headData = head.get(i);
                // 每一个List对应一个sheet页的数据
                List<T> list = exportData.get(i);
                WriteSheet sheet = EasyExcel.writerSheet(i, CollectionUtil.isEmpty(sheetNames) ? Constant.SHEET_DESC + i + 1 : sheetNames.get(i)).head(headData.getClass()).build();
                writer.write(list, sheet);
            }
        } catch (Exception e) {
            log.error("ExcelUtil exportFile excel文件 in error:{}", e);
        } finally {
            if (null != writer) {
                writer.finish();
            }
        }
    }

    /**
     * @param fileName
     * @param head
     * @param exportData
     * @param sheetNames
     */
    public void writeFile(String fileName, List<T> head, List<List<T>> exportData, List<String> sheetNames) {

        ExcelWriter writer = null;
        try {

            writer = EasyExcel.write(fileName).build();
            for (int i = 0; i < exportData.size(); i++) {
                // 获取表头数据
                Object headData = head.get(i);
                // 每一个List对应一个sheet页的数据
                List<T> list = exportData.get(i);
                WriteSheet sheet = EasyExcel.writerSheet(i, CollectionUtil.isEmpty(sheetNames) ? Constant.SHEET_DESC + i + 1 : sheetNames.get(i)).head(headData.getClass()).build();
                writer.write(list, sheet);
            }
        } catch (Exception e) {
            log.error("exportFile exportFile excel文件 in error:{}", e);
        } finally {
            if (null != writer) {
                writer.finish();
            }
        }

    }

    /**
     * 导入服务器上的模板文件
     *
     * @param formLocal 是否初始模板文件
     * @param response  response
     * @param fileName  文件名
     * @param filePath  文件路径
     * @throws Exception
     */
    public void writeFile(boolean formLocal, HttpServletResponse response, String fileName, String filePath) throws Exception {

        response.setContentType(Constant.EXCEL_CONTENT_TYPE);
        response.setCharacterEncoding(Constant.UTF_8);
        response.setHeader(Constant.CONTENT_DISPOSITION, Constant.ATTACHMENT_FILENAME + fileName);

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream fileStream = null;
        log.info(filePath);
        try {

            if (formLocal) {
                fileStream = new ClassPathResource(filePath).getStream();
            } else {
                fileStream = FileUtil.getInputStream(filePath);
            }

            bis = new BufferedInputStream(fileStream);
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[10240];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
            IOUtils.closeQuietly(fileStream);
        }
    }


    /**
     * 校验Excel表头
     *
     * @param clazz
     * @param headMap
     * @return
     * @throws Exception
     */
    public static Boolean checkExcelHead(Class clazz, Map<Integer, String> headMap, AnalysisContext context) throws Exception {

        if (BeanUtil.isEmpty(headMap)) {
            throw new ExcelAnalysisException("解析出错,未获取到excel模板表头列!");
        }

        Integer excelCount = ((XlsxReadSheetHolder) context.currentReadHolder()).getApproximateTotalRowNumber();

        if (excelCount <= 1) {
            throw new ExcelAnalysisException("解析出错,excel页签数据不能为空!");
        }

        Map<Integer, String> result = new HashMap<>();

        Field field;

        Field[] fields = clazz.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            field = clazz.getDeclaredField(fields[i].getName());

            field.setAccessible(true);

            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);

            if (excelProperty != null) {
                int index = excelProperty.index();

                String[] values = excelProperty.value();

                StringBuilder value = new StringBuilder();

                for (String v : values) {
                    value.append(v);

                }

                result.put(index, value.toString());

            }

        }
        Set<Integer> keySet = result.keySet();

        for (Integer key : keySet) {
            if (StringUtils.isEmpty(headMap.get(key))) {
                throw new ExcelAnalysisException("解析出错,excel模板第" + key + "列不存在!");

            }

            if (!headMap.get(key).equals(result.get(key))) {
                throw new ExcelAnalysisException("解析出错,第" + key + "列【" + headMap.get(key) + "】与excel模板值【" + result.get(key) + "】不匹配!");
            }
        }

        return true;

    }

    /**
     * 返回默认的excel表格样式对象
     *
     * @return
     */
    private HorizontalCellStyleStrategy getExportDefaultStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    /**
     * 导出动态表头数据(支持多表单)
     *
     * @param fileName
     * @param head
     * @param exportData
     * @param sheetNames
     * @param response
     */
    public void exportWithDynamicData(String fileName, List<List<List<String>>> head, List<List<List<T>>> exportData, List<String> sheetNames, HttpServletResponse response) throws IOException {
        ExcelWriter writer = null;
        try {
            response.setContentType(Constant.EXCEL_CONTENT_TYPE);
            response.setCharacterEncoding(Constant.UTF_8);
            response.setHeader(Constant.CONTENT_DISPOSITION, Constant.ATTACHMENT_FILENAME + fileName + Constant.XLSX_SUFFIX);
            // 获取默认样式的excel表格对象
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = getExportDefaultStyle();
            AbstractColumnWidthStyleStrategy columnWidthStyleStrategy = new SimpleColumnWidthStyleStrategy(20);
            writer = EasyExcel.write(response.getOutputStream()).registerWriteHandler(horizontalCellStyleStrategy).registerWriteHandler(columnWidthStyleStrategy).build();
            for (int i = 0; i < exportData.size(); i++) {
                List<List<T>> tableData = exportData.get(i);
                WriteSheet sheet = EasyExcel.writerSheet(i, CollectionUtil.isEmpty(sheetNames) ? Constant.SHEET_DESC + i + 1 : sheetNames.get(i)).head(head.get(i)).build();
                writer.write(tableData, sheet);
            }
        } finally {
            if (Objects.nonNull(writer)) {
                writer.finish();
            }
        }
    }
}
