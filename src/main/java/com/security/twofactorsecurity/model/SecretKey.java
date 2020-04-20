package com.security.twofactorsecurity.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class SecretKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @OneToOne
    private User user;

    private SecretKey() {
    }

    public static class Builder{
        private String code;
        private User user;

        public Builder code(String code){
            this.code = code;
            return this;
        }

        public Builder user(User user){
            this.user = user;
            return this;
        }

        public SecretKey build(){
            SecretKey secretKey = new SecretKey();
            secretKey.code = this.code;
            secretKey.user = this.user;
            return secretKey;
        }
    }
}
