import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RendezVous {
  private http= inject(HttpClient);
  private apiUrl='http://localhost:8888/api/rendez-vous'
   private apiUrls = 'http://localhost:8887/api/patient';

  ajoutRendezVous(rendezVous:any): Observable<any>{
    return this.http.post(`${this.apiUrl}`,rendezVous);
  }
  
  listerPatient(): Observable<any>{
   return this.http.get(`${this.apiUrls}`)
  }
  listerRendezVous(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/liste`);
  }
  changerStatut(id: number, nouveauStatut: string): Observable<any> {
    const params = new HttpParams().set('nouveauStatut', nouveauStatut);
    return this.http.patch<any>(`${this.apiUrl}/${id}/statut`, null, { params });
  }
   obtenir(id: string): Observable<any> {
    return this.http.get(`${this.apiUrls}/${id}`);
  }
}
