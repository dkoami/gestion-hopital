import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class Medicament {
  private http = inject(HttpClient)
  private apiUrl = 'http://localhost:8888/api/medicament';
  private apiUrls = 'http://localhost:8887/api/patient';
  ajouter(medicament: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/ajout`, medicament)
  }
 obtenir(id: string): Observable<any> {
    return this.http.get(`${this.apiUrls}/${id}`);
  }
   listerPatient(): Observable<any>{
    return this.http.get(`${this.apiUrls}`)
   }
   lister(): Observable<any>{
    return this.http.get(`${this.apiUrl}/liste`)
   }
   rechercher(mc: string): Observable<any> {
    const params = new HttpParams().set('mc', mc);
    return this.http.get(`${this.apiUrl}/recherche`, { params });
  }
}
