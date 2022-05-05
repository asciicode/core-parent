package nz.co.logicons.tlp.core.genericmodels.nodevalue;

import java.io.InputStream;

import org.apache.commons.io.FileUtils;

public class Attachment
{
  private String attachmentid;

  private String filename;
  
  private String contenttype;

  private long length;
  
  private String MD5;
  
  private InputStream inputStream;
  
  public InputStream getInputStream()
  {
    return inputStream;
  }

  public void setInputStream(InputStream inputStream)
  {
    this.inputStream = inputStream;
  }


  public String getAttachmentid()
  {
    return attachmentid;
  }

  public void setAttachmentid(String attachmentid)
  {
    this.attachmentid = attachmentid;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename(String filename)
  {
    this.filename = filename;
  }

  public String getContenttype()
  {
    return contenttype;
  }

  public void setContenttype(String contenttype)
  {
    this.contenttype = contenttype;
  }

  public String getMD5()
  {
    return MD5;
  }

  public void setMD5(String mD5)
  {
    MD5 = mD5;
  }
  
  public long getLength()
  {
    return length;
  }

  public void setLength(long length)
  {
    this.length = length;
  }
  
  public String getFormattedfilename()
  {
    return String.format("%s (%s)", filename, FileUtils.byteCountToDisplaySize(length));
  }
}
