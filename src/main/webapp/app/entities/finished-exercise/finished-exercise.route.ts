import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FinishedExercise } from 'app/shared/model/finished-exercise.model';
import { FinishedExerciseService } from './finished-exercise.service';
import { FinishedExerciseComponent } from './finished-exercise.component';
import { FinishedExerciseDetailComponent } from './finished-exercise-detail.component';
import { FinishedExerciseUpdateComponent } from './finished-exercise-update.component';
import { FinishedExerciseDeletePopupComponent } from './finished-exercise-delete-dialog.component';
import { IFinishedExercise } from 'app/shared/model/finished-exercise.model';

@Injectable({ providedIn: 'root' })
export class FinishedExerciseResolve implements Resolve<IFinishedExercise> {
  constructor(private service: FinishedExerciseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFinishedExercise> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<FinishedExercise>) => response.ok),
        map((finishedExercise: HttpResponse<FinishedExercise>) => finishedExercise.body)
      );
    }
    return of(new FinishedExercise());
  }
}

export const finishedExerciseRoute: Routes = [
  {
    path: '',
    component: FinishedExerciseComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FinishedExerciseDetailComponent,
    resolve: {
      finishedExercise: FinishedExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FinishedExerciseUpdateComponent,
    resolve: {
      finishedExercise: FinishedExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FinishedExerciseUpdateComponent,
    resolve: {
      finishedExercise: FinishedExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const finishedExercisePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FinishedExerciseDeletePopupComponent,
    resolve: {
      finishedExercise: FinishedExerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedExercise.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
