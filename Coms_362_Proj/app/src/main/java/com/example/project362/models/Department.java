package com.example.project362.models;

public class Department
{
	private String key;
	private String name;

	public Department(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public String setName(String name)
	{
		this.name = name;
		// TODO
		return null;
	}
}
