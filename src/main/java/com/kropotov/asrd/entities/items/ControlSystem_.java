package com.kropotov.asrd.entities.items;

import com.kropotov.asrd.entities.User;

import com.kropotov.asrd.entities.docs.ActInputControl;
import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.Invoice;
import com.kropotov.asrd.entities.enums.Location;
import com.kropotov.asrd.entities.enums.Status;
import com.kropotov.asrd.entities.titles.SystemTitle;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import java.time.LocalDateTime;

@StaticMetamodel(ControlSystem.class)
public class ControlSystem_ {
	public static volatile SingularAttribute<ControlSystem, Long> id;
	public static volatile SingularAttribute<ControlSystem, Status> entityStatus;
	public static volatile SingularAttribute<ControlSystem, LocalDateTime> createdAt;
	public static volatile SingularAttribute<ControlSystem, LocalDateTime> updatedAt;
	public static volatile SingularAttribute<ControlSystem, String> number;
	public static volatile SingularAttribute<ControlSystem, Location> location;
	public static volatile SingularAttribute<ControlSystem, String> purpose;
	public static volatile SingularAttribute<ControlSystem, String> purposePassport;
	public static volatile SingularAttribute<ControlSystem, LocalDate> vintage;
	public static volatile SingularAttribute<ControlSystem, Integer> vpNumber;
	public static volatile SingularAttribute<ControlSystem, LocalDate> otkDate;
	public static volatile SingularAttribute<ControlSystem, LocalDate> vpDate;

	public static volatile SingularAttribute<ControlSystem, SystemTitle> title;
	public static volatile SingularAttribute<ControlSystem, User> user;
	public static volatile ListAttribute<ControlSystem, ActInputControl> actsInputControl;
	public static volatile ListAttribute<ControlSystem, Invoice> invoices;
	public static volatile ListAttribute<ControlSystem, Device> devices;
	public static volatile ListAttribute<ControlSystem, File> files;
}
