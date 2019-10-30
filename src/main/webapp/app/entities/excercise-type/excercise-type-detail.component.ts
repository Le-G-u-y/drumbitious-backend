import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExcerciseType } from 'app/shared/model/excercise-type.model';

@Component({
  selector: 'jhi-excercise-type-detail',
  templateUrl: './excercise-type-detail.component.html'
})
export class ExcerciseTypeDetailComponent implements OnInit {
  excerciseType: IExcerciseType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ excerciseType }) => {
      this.excerciseType = excerciseType;
    });
  }

  previousState() {
    window.history.back();
  }
}
