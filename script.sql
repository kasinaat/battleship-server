CREATE table user(
    username    varchar(20)     NOT NULL,
    pass        varchar(20)     NOT NULL,
    firstname   varchar(20)     NOT NULL,
    lastname    varchar(20)     NOT NULL,
    mail_id     varchar(50)     NOT NULL UNIQUE,
    auth_key    varchar(100)    NOT NULL UNIQUE,
    mobile      bigint          NOT NULL,
    PRIMARY KEY(username));

CREATE table game(
    game_id     INT UNSIGNED    NOT NULL    AUTO_INCREMENT,
    time_played DATETIME        NOT NULL,
    creator     varchar(20)     NOT NULL,
    game_status ENUM('NEW','PLAYING','COMPLETED')   NOT NULL    default 'NEW',
    PRIMARY KEY(game_id)
);

CREATE table player_game(
    username    varchar(20)     NOT NULL,
    game_id     INT UNSIGNED    NOT NULL,
    score       INT             NOT NULL    default 0,
    PRIMARY KEY(username,game_id),

    FOREIGN KEY(username) REFERENCES user(username),
    FOREIGN KEY(game_id) REFERENCES game(game_id)
);
