import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExcerciseConfig } from 'app/shared/model/excercise-config.model';

@Component({
  selector: 'jhi-excercise-config-detail',
  templateUrl: './excercise-config-detail.component.html'
})
export class ExcerciseConfigDetailComponent implements OnInit {
  excerciseConfig: IExcerciseConfig;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ excerciseConfig }) => {
      this.excerciseConfig = excerciseConfig;
    });
  }

  previousState() {
    window.history.back();
  }
}
