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
        path: 'exercise-config',
        loadChildren: () => import('./exercise-config/exercise-config.module').then(m => m.DrumbitiousBackendExerciseConfigModule)
      },
      {
        path: 'exercise',
        loadChildren: () => import('./exercise/exercise.module').then(m => m.DrumbitiousBackendExerciseModule)
      },
      {
        path: 'finished-session',
        loadChildren: () => import('./finished-session/finished-session.module').then(m => m.DrumbitiousBackendFinishedSessionModule)
      },
      {
        path: 'finished-exercise',
        loadChildren: () => import('./finished-exercise/finished-exercise.module').then(m => m.DrumbitiousBackendFinishedExerciseModule)
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
