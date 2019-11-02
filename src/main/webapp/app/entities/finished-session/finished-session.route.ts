import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FinishedSession } from 'app/shared/model/finished-session.model';
import { FinishedSessionService } from './finished-session.service';
import { FinishedSessionComponent } from './finished-session.component';
import { FinishedSessionDetailComponent } from './finished-session-detail.component';
import { FinishedSessionUpdateComponent } from './finished-session-update.component';
import { FinishedSessionDeletePopupComponent } from './finished-session-delete-dialog.component';
import { IFinishedSession } from 'app/shared/model/finished-session.model';

@Injectable({ providedIn: 'root' })
export class FinishedSessionResolve implements Resolve<IFinishedSession> {
  constructor(private service: FinishedSessionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFinishedSession> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<FinishedSession>) => response.ok),
        map((finishedSession: HttpResponse<FinishedSession>) => finishedSession.body)
      );
    }
    return of(new FinishedSession());
  }
}

export const finishedSessionRoute: Routes = [
  {
    path: '',
    component: FinishedSessionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FinishedSessionDetailComponent,
    resolve: {
      finishedSession: FinishedSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FinishedSessionUpdateComponent,
    resolve: {
      finishedSession: FinishedSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FinishedSessionUpdateComponent,
    resolve: {
      finishedSession: FinishedSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedSession.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const finishedSessionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FinishedSessionDeletePopupComponent,
    resolve: {
      finishedSession: FinishedSessionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.finishedSession.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
