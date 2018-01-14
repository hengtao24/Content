package com.sht.content.model.reading;



import java.io.Serializable;

/**
 * Created by sht on 2017/3/9.
 */

public class ReadingBean implements Serializable{
//    使用注释部分代码，不能正常运行？
 /*   private BookBean boo[];

    public  BookBean[] getBoo(){
        return boo;
    }

    public void setBoo(BookBean[] boo){
        this.boo = boo;
    }
*/
    private BookBean books[];

    public BookBean[] getBooks() {
        return books;
    }

    public void setBooks(BookBean[] books) {
        this.books = books;
    }
}
