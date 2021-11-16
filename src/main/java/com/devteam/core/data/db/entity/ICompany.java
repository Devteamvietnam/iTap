package com.devteam.core.data.db.entity;

public interface ICompany {
    static public ICompany MOCK = new MockCompany();

    public String getId();
    public String getCode() ;
    public String getLabel() ;

    static public class MockCompany implements ICompany {

        public String getId() { return String.valueOf(1L); };

        @Override
        public String getCode() {
            return "mock";
        }

        @Override
        public String getLabel() { return "A mock company"; }

    }
}
