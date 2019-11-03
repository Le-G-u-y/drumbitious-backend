import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { DrummerStatistics } from 'app/shared/model/drummer-statistics.model';
import { DrummerStatisticsService } from './drummer-statistics.service';
import { DrummerStatisticsComponent } from './drummer-statistics.component';
import { DrummerStatisticsDetailComponent } from './drummer-statistics-detail.component';
import { DrummerStatisticsUpdateComponent } from './drummer-statistics-update.component';
import { DrummerStatisticsDeletePopupComponent } from './drummer-statistics-delete-dialog.component';
import { IDrummerStatistics } from 'app/shared/model/drummer-statistics.model';

@Injectable({ providedIn: 'root' })
export class DrummerStatisticsResolve implements Resolve<IDrummerStatistics> {
  constructor(private service: DrummerStatisticsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDrummerStatistics> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<DrummerStatistics>) => response.ok),
        map((drummerStatistics: HttpResponse<DrummerStatistics>) => drummerStatistics.body)
      );
    }
    return of(new DrummerStatistics());
  }
}

export const drummerStatisticsRoute: Routes = [
  {
    path: '',
    component: DrummerStatisticsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.drummerStatistics.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DrummerStatisticsDetailComponent,
    resolve: {
      drummerStatistics: DrummerStatisticsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.drummerStatistics.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DrummerStatisticsUpdateComponent,
    resolve: {
      drummerStatistics: DrummerStatisticsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.drummerStatistics.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DrummerStatisticsUpdateComponent,
    resolve: {
      drummerStatistics: DrummerStatisticsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.drummerStatistics.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const drummerStatisticsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DrummerStatisticsDeletePopupComponent,
    resolve: {
      drummerStatistics: DrummerStatisticsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'drumbitiousBackendApp.drummerStatistics.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
