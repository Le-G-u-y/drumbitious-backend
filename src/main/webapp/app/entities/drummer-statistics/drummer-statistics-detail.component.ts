import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDrummerStatistics } from 'app/shared/model/drummer-statistics.model';

@Component({
  selector: 'jhi-drummer-statistics-detail',
  templateUrl: './drummer-statistics-detail.component.html'
})
export class DrummerStatisticsDetailComponent implements OnInit {
  drummerStatistics: IDrummerStatistics;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ drummerStatistics }) => {
      this.drummerStatistics = drummerStatistics;
    });
  }

  previousState() {
    window.history.back();
  }
}
