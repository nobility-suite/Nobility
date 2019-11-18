package net.civex4.nobility.group;

public enum GroupPermission {
  
  DEFAULT("Member"),
  TRUSTED("Trusted"),
  OFFICER("Official"),
  LEADER("Leader");
  
  private final String rank;

  GroupPermission(String rank){
    this.rank = rank;
  }
}
