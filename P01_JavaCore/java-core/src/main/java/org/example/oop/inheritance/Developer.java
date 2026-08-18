package org.example.oop.inheritance;

class Developer extends Employee{
    private int overTimeHours;

    public Developer(String name, double salary, int overTimeHours) {
        super(name, salary);
        this.overTimeHours = overTimeHours;
    }

    public Developer() {
    }

    public int getOverTimeHours() {
        return overTimeHours;
    }

    public void setOverTimeHours(int overTimeHours) {
        this.overTimeHours = overTimeHours;
    }

    @Override
    protected double calculateSalary() {
        return getSalary() + overTimeHours * 100;
    }
}
