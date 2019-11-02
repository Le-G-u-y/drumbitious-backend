import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinishedExercise } from 'app/shared/model/finished-exercise.model';

@Component({
  selector: 'jhi-finished-exercise-detail',
  templateUrl: './finished-exercise-detail.component.html'
})
export class FinishedExerciseDetailComponent implements OnInit {
  finishedExercise: IFinishedExercise;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finishedExercise }) => {
      this.finishedExercise = finishedExercise;
    });
  }

  previousState() {
    window.history.back();
  }
}
