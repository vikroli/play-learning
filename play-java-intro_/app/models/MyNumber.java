package models;

import javax.persistence.*;

@Entity
public class MyNumber {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public long id;

    public long value;
}
