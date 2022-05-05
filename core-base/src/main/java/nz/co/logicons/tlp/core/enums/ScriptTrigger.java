package nz.co.logicons.tlp.core.enums;

import nz.co.logicons.tlp.core.genericmodels.base.EnumInterface;

/**
 * Types of triggers for scripts.
 * @author bhambr
 *
 */
public enum ScriptTrigger implements EnumInterface
{
  //Schema
  BeforeSaveServer,
  AfterSaveServer,
  BeforeDeleteServer,
  AfterDeleteServer,
  OnValidationServer,
  PermissionServer,
  
  AfterCancelServer,
  //View
  OnLoadBrowser,
  ButtonBrowser,
  FunctionButtonBrowser,
  FileButtonBrowser,
  BeforeAddNewClick,
  //ViewField
  OnChangeBrowser,
  AfterNewFromSearchBrowser,
  AfterDeleteLineBrowser,
  //GridColumn (type : calculated) or GridTab (type: calculatedfield/calculatedgrid)
  OnRender;
}
