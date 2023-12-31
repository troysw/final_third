package com.finalThird.finalThird.premierLeague.domain;

import com.finalThird.finalThird.common.entity.BaseEntity;
import com.finalThird.finalThird.customer.domain.Customer;
import com.finalThird.finalThird.premierLeague.controller.dto.EplRequest;
import com.finalThird.finalThird.premierLeague.controller.dto.EplResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter(AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor
public class PremierLeagueBoard extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long boardId;

  private String title;

  private String content;

  private LocalDateTime createdDate;

  private Long commentCount;

  private int views;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "fk__board__customer"))
  private Customer createUser;

  @ManyToOne
  @JoinColumn(name = "team_id")
  private PremierLeagueTeam team;

  @OneToMany(mappedBy = "board")
  private List<BoardComment> commentList = new ArrayList<>();

  public static PremierLeagueBoard create(EplRequest.BoardRequest request, PremierLeagueTeam team, Customer createUser) {
    PremierLeagueBoard res = new PremierLeagueBoard();
    res.setCreateUser(createUser);
    res.setTitle(request.getTitle());
    res.setContent(request.getContent());
    res.setCreatedDate(LocalDateTime.now());
    res.setTeam(team);
    return res;
  }


  public EplResponse.boardListResponse toResponse(){
    EplResponse.boardListResponse res = new EplResponse.boardListResponse();
    res.setBoardId(this.boardId);
    res.setCustomerNickName(this.getCreateUser().getNickName());
    res.setTitle(this.getTitle());
    res.setCreatedDate(this.getCreatedDate());
    res.setViews(this.getViews());
    return res;
  }


  public void modify(EplRequest.BoardPatchRequest request) {
    this.title = request.getTitle();
    this.content = request.getContent();
  }
}
