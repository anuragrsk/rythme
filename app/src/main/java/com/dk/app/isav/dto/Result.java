package com.dk.app.isav.dto;



import java.io.Serializable;
import java.util.HashMap;

public class Result
  implements Serializable
{
  private static final long serialVersionUID = -6977435562653463669L;
  private HashMap<String, String> additionalData;
  private String alertType;
  private boolean donotKeepScreenHistory = false;
  private String failureMessage;
  private boolean isSuccess = false;
  private boolean isToNavigateTonextScreen;
  private boolean logoutUser = false;
  private Class<?> nextScreen;
  private int setFocusOn = -1;
  private String successMessage;

  public HashMap<String, String> getAdditionalData()
  {
    return this.additionalData;
  }

  public String getAlertType()
  {
    return this.alertType;
  }

  public String getFailureMessage()
  {
    return this.failureMessage;
  }

  public Class<?> getNextScreen()
  {
    return this.nextScreen;
  }

  public int getSetFocusOn()
  {
    return this.setFocusOn;
  }

  public String getSuccessMessage()
  {
    return this.successMessage;
  }

  public boolean isDonotKeepScreenHistory()
  {
    return this.donotKeepScreenHistory;
  }

  public boolean isLogoutUser()
  {
    return this.logoutUser;
  }

  public boolean isSuccess()
  {
    return this.isSuccess;
  }

  public boolean isToNavigateTonextScreen()
  {
    return this.isToNavigateTonextScreen;
  }

  public void setAdditionalData(HashMap<String, String> paramHashMap)
  {
    this.additionalData = paramHashMap;
  }

  public void setAlertType(String paramString)
  {
    this.alertType = paramString;
  }

  public void setDonotKeepScreenHistory(boolean paramBoolean)
  {
    this.donotKeepScreenHistory = paramBoolean;
  }

  public void setFailureMessage(String paramString)
  {
    this.failureMessage = paramString;
  }

  public void setLogoutUser(boolean paramBoolean)
  {
    this.logoutUser = paramBoolean;
  }

  public void setNextScreen(Class<?> paramClass)
  {
    this.nextScreen = paramClass;
  }

  public void setSetFocusOn(int paramInt)
  {
    this.setFocusOn = paramInt;
  }

  public void setSuccess(boolean paramBoolean)
  {
    this.isSuccess = paramBoolean;
  }

  public void setSuccessMessage(String paramString)
  {
    this.successMessage = paramString;
  }

  public void setToNavigateTonextScreen(boolean paramBoolean)
  {
    this.isToNavigateTonextScreen = paramBoolean;
  }

  public String toString()
  {
    return "Result [isSuccess=" + this.isSuccess + ", successMessage=" + this.successMessage + ", failureMessage=" + this.failureMessage + ", nextScreen=" + this.nextScreen + ", isToNavigateTonextScreen=" + this.isToNavigateTonextScreen + ", logoutUser=" + this.logoutUser + ", setFocusOn=" + this.setFocusOn + ", donotKeepScreenHistory=" + this.donotKeepScreenHistory + "]";
  }
}