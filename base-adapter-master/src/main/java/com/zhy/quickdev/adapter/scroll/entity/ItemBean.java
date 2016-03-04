package com.zhy.quickdev.adapter.scroll.entity;

public class ItemBean
{

	private String title;
	private String desc;
	private String time;
	private String phone;

	private boolean isVisible;
	private boolean isChecked ;




	public boolean isChecked()
	{
		return isChecked;
	}

	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}

	public ItemBean()
	{
	}

	public ItemBean(String title, String desc, String time, String phone)
	{
		super();
		this.title = title;
		this.desc = desc;
		this.time = time;
		this.phone = phone;
		this.isChecked = false;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
