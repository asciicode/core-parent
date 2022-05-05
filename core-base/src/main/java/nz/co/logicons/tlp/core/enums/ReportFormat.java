package nz.co.logicons.tlp.core.enums;
// package nz.co.spikydev.base.enums;
//
// import com.google.common.net.MediaType;
//
// import net.sf.jasperreports.engine.JRAbstractExporter;
// import net.sf.jasperreports.engine.export.HtmlExporter;
// import net.sf.jasperreports.engine.export.JRCsvExporter;
// import net.sf.jasperreports.engine.export.JRPdfExporter;
// import net.sf.jasperreports.engine.export.JRRtfExporter;
// import net.sf.jasperreports.engine.export.JRXlsExporter;
// import net.sf.jasperreports.engine.export.JRXmlExporter;
// import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
// import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
// import net.sf.jasperreports.engine.util.LocalJasperReportsContext;
// import nz.co.spikydev.base.jasper.operations.QuotedCsvExporter;
//
// public enum ReportFormat
// {
// PDF(".pdf", MediaType.PDF),
// CSVQ(".csv", MediaType.CSV_UTF_8),
// TXTQ(".txt", MediaType.CSV_UTF_8),
// CSV(".csv", MediaType.CSV_UTF_8),
// RTF(".rtf", MediaType.RTF_UTF_8),
// DOCX(".docx", MediaType.MICROSOFT_WORD),
// XLS(".xls", MediaType.MICROSOFT_EXCEL),
// XLSX(".xlsx", MediaType.MICROSOFT_EXCEL),
// HTML(".html", MediaType.HTML_UTF_8),
// XML(".xml", MediaType.XML_UTF_8 ),
// PNG(".png", MediaType.PNG );
//
// private final String extension;
//
// private final MediaType mediaType;
//
// private ReportFormat(String extension, MediaType mediaType)
// {
// this.extension = extension;
// this.mediaType = mediaType;
// }
//
// public String getExtension()
// {
// return extension;
// }
//
// public MediaType getMediaType()
// {
// return mediaType;
// }
//
// public JRAbstractExporter getExporter(LocalJasperReportsContext jasperReportsContext)
// {
// switch (this)
// {
// case TXTQ:
// return new QuotedCsvExporter(jasperReportsContext);
// case CSVQ:
// return new QuotedCsvExporter(jasperReportsContext);
// case CSV:
// return new JRCsvExporter(jasperReportsContext);
// case PDF:
// return new JRPdfExporter(jasperReportsContext);
// case RTF:
// return new JRRtfExporter(jasperReportsContext);
// case DOCX:
// return new JRDocxExporter(jasperReportsContext);
// case XLS:
// return new JRXlsExporter(jasperReportsContext);
// case XLSX:
// return new JRXlsxExporter(jasperReportsContext);
// case HTML:
// return new HtmlExporter(jasperReportsContext);
// case XML:
// return new JRXmlExporter(jasperReportsContext);
// default:
// return null;
// }
// }
//
// }
