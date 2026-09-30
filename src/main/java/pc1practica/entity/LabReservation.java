package pc1practica.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "reservations")
public class LabReservation{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private Long slotId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private ZonedDateTime reservedAt;

    @Column(nullable = false)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public ZonedDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(ZonedDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
