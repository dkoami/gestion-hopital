import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root',
})
export class Constante {

  private http= inject(HttpClient)
  private apiUrl = 'http://localhost:8886/api/constante';
  private apiUrls = 'http://localhost:8887/api/patient';

  ajouter(pid: string,constant:any): Observable<any>{
    return this.http.post(`${this.apiUrl}/${pid}`, constant);
  }
    obtenir(id: string): Observable<any> {
    return this.http.get(`${this.apiUrls}/${id}`);
  }
  lister(): Observable<any>{
    return this.http.get(`${this.apiUrl}/liste`)
  }
   obtenirUneconstante(id: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
   modifier(id: string, constante: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, constante);
  }
}
