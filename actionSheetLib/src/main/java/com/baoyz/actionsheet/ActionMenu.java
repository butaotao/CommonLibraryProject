package com.baoyz.actionsheet;
/**
 * 弹出的菜单
 * @author WANG
 *
 */
public class ActionMenu {
	/**
	 * 图标的资源Id
	 */
	private int resourceId;
	private String title;
	private boolean isNeedTag;
	
	

	public ActionMenu(int resourceId, String title, boolean isNeedTag) {
		super();
		this.resourceId = resourceId;
		this.title = title;
		this.isNeedTag = isNeedTag;
	}
	public int getResourceId() {
		return resourceId;
	}
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isNeedTag() {
		return isNeedTag;
	}
	public void setNeedTag(boolean isNeedTag) {
		this.isNeedTag = isNeedTag;
	}

	
	
	
}
