import { Routes } from '@angular/router';
import { Acceuil } from './components/acceuil/acceuil';
import { PatientListe } from './components/patient-liste/patient-liste';
import { AjoutPatient } from './components/ajout-patient/ajout-patient';
import { BonListe } from './components/bon-liste/bon-liste';
import { AjoutBon } from './components/ajout-bon/ajout-bon';
import { MiseAjourPatient } from './components/mise-ajour-patient/mise-ajour-patient';
import { MiseAjourBon } from './components/mise-ajour-bon/mise-ajour-bon';
import { AjoutConstante } from './components/ajout-constante/ajout-constante';
import { ConstanteListe } from './components/constante-liste/constante-liste';
import { MiseAjourConstante } from './components/mise-ajour-constante/mise-ajour-constante';
import { AjoutConsultation } from './components/ajout-consultation/ajout-consultation';
import { MenuPrincipal } from './components/menu-principal/menu-principal';
import { ConsultationListeComponent } from './components/consultation-liste/consultation-liste';
import { Vente } from './components/vente/vente';
import { AjoutRendezVous } from './components/ajout-rendez-vous/ajout-rendez-vous';
import { RendezVousAgendaComponent } from './components/rendez-vous-liste/rendez-vous-liste';
import { AjoutMdicament } from './components/ajout-mdicament/ajout-mdicament';
import { MedicamentListe } from './components/medicament-liste/medicament-liste';
import { AjoutSejourComponent } from './components/ajout-sejour/ajout-sejour';
import { SejourListe } from './components/sejour-liste/sejour-liste';

export const routes: Routes = [
    {path:'',component:Acceuil,
     children: [
      { path: 'patient', component: PatientListe ,},
      { path: 'ajoutPatient', component: AjoutPatient },
      { path: 'bon', component: BonListe },
      { path: 'ajoutBon', component: AjoutBon },
      {path: 'modifierPatient/:pid', component: MiseAjourPatient},
      {path:'modifierBon/:bid', component: MiseAjourBon},
      {path:'ajoutConstante/:pid', component: AjoutConstante},
      {path:'constante', component: ConstanteListe},
      {path:'modifierConstante/:constid', component: MiseAjourConstante},
      {path: 'ajoutConsultation', component: AjoutConsultation},
      {path: 'principal', component: MenuPrincipal},
      {path: 'consultations', component: ConsultationListeComponent},
      {path: 'vente', component: Vente},
      {path: 'ajoutRendezVous/:pid', component: AjoutRendezVous},
      {path: 'rendezVous', component: RendezVousAgendaComponent},
      {path: 'ajoutMedicament/:pid', component: AjoutMdicament},
      {path: 'medicaments', component: MedicamentListe},
      {path: 'ajoutSejour/:cid/:pid', component: AjoutSejourComponent},
      {path: 'sejour', component: SejourListe}
    ]
    }
];
