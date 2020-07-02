package com.kropotov.asrd.entities.items;

import com.kropotov.asrd.entities.User;
import com.kropotov.asrd.entities.common.IFiles;
import com.kropotov.asrd.entities.common.PageableEntity;
import com.kropotov.asrd.entities.docs.ActInputControl;
import com.kropotov.asrd.entities.docs.File;
import com.kropotov.asrd.entities.docs.Invoice;
import com.kropotov.asrd.entities.enums.Location;
import com.kropotov.asrd.entities.enums.Status;
import com.kropotov.asrd.entities.titles.SystemTitle;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "systems")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Audited(withModifiedFlag = true)
public class ControlSystem implements PageableEntity, IFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Number cannot be null")
    @Column(name = "number")
    private String number;

    @Enumerated(value = EnumType.ORDINAL)
    @Value("0")
    private Location location;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "purpose_passport")
    private String purposePassport;

    @Column(name = "vintage")
    private LocalDate vintage;

    @Column(name = "vp_number")
    private int vpNumber;

    @Audited(modifiedColumnName = "accept_otk_date")
    @Column(name = "accept_otk_date")
    private LocalDate otkDate;

    @Audited(modifiedColumnName = "accept_vp_date")
    @Column(name = "accept_vp_date")
    private LocalDate vpDate;

    @Enumerated(value = EnumType.ORDINAL)
    private Status entityStatus;

    @Version
    private int version;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @NotAudited
    // @NotNull(message = "Title cannot be null")
    @ManyToOne
    @JoinColumn(name = "title_system_id")
    private SystemTitle title;

    @NotNull(message = "User cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Audited(modifiedColumnName = "user_id")
    private User user;


    @NotAudited
    @ManyToMany(mappedBy = "systems", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<ActInputControl> actsInputControl;

    @NotAudited
    @ManyToMany(mappedBy = "systems", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Invoice> invoices;

    @NotAudited
    @OneToMany(mappedBy = "system", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<Device> devices;

    @NotAudited
    @ManyToMany
    @JoinTable(
            name = "system_files",
            joinColumns = @JoinColumn(name = "system_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files;

    public void addDevice(Device device) {
        if (devices == null) {
            devices = new ArrayList<>();
        }
        devices.add(device);
    }

    @Override
    public boolean addFile(File file) {
        return files.add(file);
    }

    @Override
    public boolean removeFile(File file) {
        return files.remove(file);
    }
}
