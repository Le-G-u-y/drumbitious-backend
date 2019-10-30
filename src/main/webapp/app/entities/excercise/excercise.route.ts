import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Excercise } from 'app/shared/model/excercise.model';
import { ExcerciseService } from './excercise.service';
import { ExcerciseComponent } from './excercise.component';
import { ExcerciseDetailComponent } from './excercise-detail.component';
import { ExcerciseUpdateComponent } from './excercise-update.component';
import { ExcerciseDeletePopupComponent } from './excercise-delete-dialog.component';
import { IExcercise } from 'app/shared/model/excercise.model';

@Injectable({ providedIn: 'root' })
export class ExcerciseResolve implements Resolve<IExcercise> {
  constructor(private service: ExcerciseService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IExcercise> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Excercise>) => response.ok),
        map((excercise: HttpResponse<Excercise>) => excercise.body)
      );
    }
    return of(new Excercise());
  }
}

export const excerciseRoute: Routes = [
  {
    path: '',
    component: ExcerciseComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ExcerciseDetailComponent,
    resolve: {
      excercise: ExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ExcerciseUpdateComponent,
    resolve: {
      excercise: ExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ExcerciseUpdateComponent,
    resolve: {
      excercise: ExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excercise.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const excercisePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ExcerciseDeletePopupComponent,
    resolve: {
      excercise: ExcerciseResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.excercise.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
