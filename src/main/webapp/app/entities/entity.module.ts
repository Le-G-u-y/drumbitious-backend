import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'i-18-n-text',
        loadChildren: () => import('./i-18-n-text/i-18-n-text.module').then(m => m.DrumbitiousBackendI18nTextModule)
      },
      {
        path: 'plan',
        loadChildren: () => import('./plan/plan.module').then(m => m.DrumbitiousBackendPlanModule)
      },
      {
        path: 'excercise-config',
        loadChildren: () => import('./excercise-config/excercise-config.module').then(m => m.DrumbitiousBackendExcerciseConfigModule)
      },
      {
        path: 'excercise',
        loadChildren: () => import('./excercise/excercise.module').then(m => m.DrumbitiousBackendExcerciseModule)
      },
      {
        path: 'skill-type',
        loadChildren: () => import('./skill-type/skill-type.module').then(m => m.DrumbitiousBackendSkillTypeModule)
      },
      {
        path: 'excercise-type',
        loadChildren: () => import('./excercise-type/excercise-type.module').then(m => m.DrumbitiousBackendExcerciseTypeModule)
      },
      {
        path: 'finished-session',
        loadChildren: () => import('./finished-session/finished-session.module').then(m => m.DrumbitiousBackendFinishedSessionModule)
      },
      {
        path: 'finished-excercise',
        loadChildren: () => import('./finished-excercise/finished-excercise.module').then(m => m.DrumbitiousBackendFinishedExcerciseModule)
      },
      {
        path: 'drummer-statistics',
        loadChildren: () => import('./drummer-statistics/drummer-statistics.module').then(m => m.DrumbitiousBackendDrummerStatisticsModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class DrumbitiousBackendEntityModule {}
