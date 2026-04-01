package ch.ivyteam.test.soap.service;

import java.util.ArrayList;
import java.util.List;

public class Country
{
  public static List<Country> countries = new ArrayList<>();
  static
  {
    countries.add(new Country("CH", "Switzerland"));
    countries.add(new Country("DE", "Germany"));
    countries.add(new Country("IT", "Italy"));
  }

  private String shortName;
  private String name;

  Country(String shortName, String name)
  {
    this.shortName = shortName;
    this.name = name;
  }

  public String getShortName()
  {
    return shortName;
  }

  public void setShortName(String shortName)
  {
    this.shortName = shortName;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
