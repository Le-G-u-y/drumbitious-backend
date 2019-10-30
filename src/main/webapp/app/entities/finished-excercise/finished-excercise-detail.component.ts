import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinishedExcercise } from 'app/shared/model/finished-excercise.model';

@Component({
  selector: 'jhi-finished-excercise-detail',
  templateUrl: './finished-excercise-detail.component.html'
})
export class FinishedExcerciseDetailComponent implements OnInit {
  finishedExcercise: IFinishedExcercise;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finishedExcercise }) => {
      this.finishedExcercise = finishedExcercise;
    });
  }

  previousState() {
    window.history.back();
  }
}
