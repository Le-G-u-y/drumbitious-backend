import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinishedSession } from 'app/shared/model/finished-session.model';

@Component({
  selector: 'jhi-finished-session-detail',
  templateUrl: './finished-session-detail.component.html'
})
export class FinishedSessionDetailComponent implements OnInit {
  finishedSession: IFinishedSession;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finishedSession }) => {
      this.finishedSession = finishedSession;
    });
  }

  previousState() {
    window.history.back();
  }
}
