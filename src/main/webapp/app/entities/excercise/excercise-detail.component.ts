import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExcercise } from 'app/shared/model/excercise.model';

@Component({
  selector: 'jhi-excercise-detail',
  templateUrl: './excercise-detail.component.html'
})
export class ExcerciseDetailComponent implements OnInit {
  excercise: IExcercise;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ excercise }) => {
      this.excercise = excercise;
    });
  }

  previousState() {
    window.history.back();
  }
}
