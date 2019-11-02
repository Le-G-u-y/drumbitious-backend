import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExerciseConfig } from 'app/shared/model/exercise-config.model';

@Component({
  selector: 'jhi-exercise-config-detail',
  templateUrl: './exercise-config-detail.component.html'
})
export class ExerciseConfigDetailComponent implements OnInit {
  exerciseConfig: IExerciseConfig;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ exerciseConfig }) => {
      this.exerciseConfig = exerciseConfig;
    });
  }

  previousState() {
    window.history.back();
  }
}
