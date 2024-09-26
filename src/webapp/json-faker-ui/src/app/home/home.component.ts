import {Component, inject, OnInit} from '@angular/core';
import {HttpResponse} from "@angular/common/http";
import {HomeService} from "./home-service";
import {IUser} from "./user/user.model";
import {RouterLink} from "@angular/router";
import {Page} from "./page.model";
import {ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER} from "../constants/pagination.constants";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  users: IUser[] = [];

  page: number = 0;
  totalCount = 0;

  private service = inject(HomeService);

  ngOnInit(): void {
    this.loadFirstPageOfUsers();
  }

  loadFirstPageOfUsers() {
    this.page = 0;
    let page = new Page(this.page, ITEMS_PER_PAGE, 'id');
    this.getUsersWithPageable(page);
  }

  trackIdentity(_index: number, item: IUser): number {
    return item.id!;
  }

  clearUsers() {
    this.users = [];
  }

  nextPage() {
    this.page++;
    let page = new Page(this.page, ITEMS_PER_PAGE, 'id');
    this.getUsersWithPageable(page);
  }

  previousPage() {
    this.page--;
    let page = new Page(this.page, ITEMS_PER_PAGE, 'id');
    this.getUsersWithPageable(page);
  }

  private getUsersWithPageable(page: Page) {
    this.service
      .getUsersWithPageable(page)
      .subscribe({
        next: (res: HttpResponse<IUser[]>) => {
          this.totalCount = Number(res.headers.get(TOTAL_COUNT_RESPONSE_HEADER));
          if (res.body) {
            this.users = res.body;
          }
        },
        error: () => console.log("error while fetching users")
      });
  }

  showNextPage():boolean{
    let nextPage = this.page + 1;
    return nextPage * ITEMS_PER_PAGE < this.totalCount;
  }


}
