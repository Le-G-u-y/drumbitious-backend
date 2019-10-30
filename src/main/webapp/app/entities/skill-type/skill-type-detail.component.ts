import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISkillType } from 'app/shared/model/skill-type.model';

@Component({
  selector: 'jhi-skill-type-detail',
  templateUrl: './skill-type-detail.component.html'
})
export class SkillTypeDetailComponent implements OnInit {
  skillType: ISkillType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ skillType }) => {
      this.skillType = skillType;
    });
  }

  previousState() {
    window.history.back();
  }
}
