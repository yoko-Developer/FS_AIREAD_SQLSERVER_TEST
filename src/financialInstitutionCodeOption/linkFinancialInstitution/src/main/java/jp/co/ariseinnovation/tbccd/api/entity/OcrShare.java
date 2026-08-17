package jp.co.ariseinnovation.tbccd.api.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
//import jp.co.ariseinnovation.tbccd.api.consts.StringConsts;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class OcrShare {
    protected OcrShare() {
    }

    /**
     * 登録時刻
     */
    @Column(name = "insertdatetime")
    private LocalDateTime insertDatetime;
    /**
     * 更新時刻
     */
    @Column(name = "updatedatetime")
    private LocalDateTime updateDatetime;
    /**
     * 更新ユーザ
     */
    @Column(name = "updateuser")
    private String updateUser;

    @PrePersist
    protected void onCreate() {
        if (Objects.isNull(insertDatetime)) {
            insertDatetime = LocalDateTime.now();
        }
        if (Objects.isNull(updateDatetime)) {
            updateDatetime = insertDatetime;
        }

        //if (Objects.isNull(updateUser)) {
        //    updateUser = StringConsts.UpdaterNameSystem;
        //}
    }

    @PreUpdate
    protected void onUpdate() {
        updateDatetime = LocalDateTime.now();
        //if (Objects.isNull(updateUser)) {
        //    updateUser = StringConsts.UpdaterNameSystem;
        //}
    }
}
