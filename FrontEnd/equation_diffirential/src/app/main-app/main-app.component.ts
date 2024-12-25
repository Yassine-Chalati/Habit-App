import { Component, ViewChild, ElementRef, AfterViewInit, OnDestroy } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import * as echarts from 'echarts';
import jsPDF from 'jspdf';
import { saveAs } from 'file-saver';
import { Router } from '@angular/router';


interface ODEResponse {
  solution: number[][];
}

interface ProfileData {
  idAccount: number;
  firstName: string;
  lastName: string;
  email: string;
  gender: string;
  birthdate: string;
  password: string | null;
}

@Component({
  selector: 'app-main',
  template: `
    <div class="main-container">
      <!-- Sidebar -->
      <div class="sidebar">
        <div class="app-title">
          <i class="fas fa-square-root-alt"></i>
          <h1>ODE Solver</h1>
        </div>
        <div class="sidebar-buttons">
          <button class="profile-btn" (click)="openProfileModal()">
            <i class="fas fa-user"></i>
            Profile
          </button>
<button class="logout-btn" (click)="logout()">
            <i class="fas fa-sign-out-alt"></i>
            Logout
          </button>
        </div>
      </div>

      <!-- Main Content -->
      <div class="main-content">
        <h2>Ordinary Differential Equation Solver</h2>
        <div class="solver-form">
          <div class="form-group">
            <label>Equation Type:</label>
            <select [(ngModel)]="odeType" class="form-control">
              <option value="first_order">First Order</option>
              <option value="second_order">Second Order</option>
            </select>
          </div>

          <div class="form-group">
            <label>X Start:</label>
            <input type="number" [(ngModel)]="xStart" class="form-control">
          </div>

          <div class="form-group">
            <label>X End:</label>
            <input type="number" [(ngModel)]="xEnd" class="form-control">
          </div>

      
          <button (click)="solve()" class="solve-btn">
            <i class="fas fa-calculator"></i>
            Solve
          </button>
        </div>

        <div class="download-buttons">
          <button (click)="downloadedPDF()" class="download-btn">
            <i class="fas fa-file-pdf"></i>
            Download PDF
          </button>
          <button (click)="downloadCSV()" class="download-btn">
            <i class="fas fa-file-csv"></i>
            Download CSV
          </button>
          <button (click)="downloadPDF(true)" class="download-btn">
            <i class="fas fa-envelope"></i>
            Send Solution To Email
          </button>
        </div>
        
        <div class="chart-container" #chartDiv></div>

     
      </div>
    </div>

     <!-- Profile Modal -->
    <div class="modal" *ngIf="showProfileModal">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Profile</h2>
          <button class="close-btn" (click)="closeProfileModal()">
            <i class="fas fa-times"></i>
          </button>
        </div>
        
        <div class="modal-body">
          <div class="profile-avatar">
            <i class="fas fa-user-circle fa-5x"></i>
          </div>
          
          <div class="profile-info" *ngIf="!isEditing">
            <div class="info-item">
              <i class="fas fa-user"></i>
              <span>{{profileData?.firstName}} {{profileData?.lastName}}</span>
            </div>
            <div class="info-item">
              <i class="fas fa-envelope"></i>
              <span>{{profileData?.email}}</span>
            </div>
            <div class="info-item">
              <i class="fas fa-venus-mars"></i>
              <span>{{profileData?.gender}}</span>
            </div>
            <div class="info-item">
              <i class="fas fa-calendar"></i>
              <span>{{profileData?.birthdate | date:'mediumDate'}}</span>
            </div>
            <button class="edit-btn" (click)="toggleEdit()">
              <i class="fas fa-edit"></i>
              Edit Profile
            </button>
          </div>

          <div class="profile-edit" *ngIf="isEditing">
            <div class="form-group">
              <label><i class="fas fa-user"></i> First Name:</label>
              <input type="text" [(ngModel)]="profileData.firstName" class="form-control">
            </div>
            <div class="form-group">
              <label><i class="fas fa-user"></i> Last Name:</label>
              <input type="text" [(ngModel)]="profileData.lastName" class="form-control">
            </div>
            <div class="form-group">
              <label><i class="fas fa-envelope"></i> Email:</label>
              <input type="email" [(ngModel)]="profileData.email" class="form-control">
            </div>
            <div class="form-group">
              <label><i class="fas fa-venus-mars"></i> Gender:</label>
              <select [(ngModel)]="profileData.gender" class="form-control">
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>
            <div class="form-group">
              <label><i class="fas fa-calendar"></i> Date of Birth:</label>
              <input type="date" [(ngModel)]="profileData.birthdate" class="form-control">
            </div>
            <div class="button-group">
              <button class="save-btn" (click)="saveProfile()">
                <i class="fas fa-save"></i>
                Save
              </button>
              <button class="cancel-btn" (click)="toggleEdit()">
                <i class="fas fa-times"></i>
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .modal {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(0, 0, 0, 0.5);
      display: flex;
      justify-content: center;
      align-items: center;
      z-index: 1000;
    }

    .modal-content {
      background: white;
      padding: 20px;
      border-radius: 8px;
      width: 90%;
      max-width: 500px;
      position: relative;
    }

    .modal-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }

    .close-btn {
      background: none;
      border: none;
      font-size: 1.5em;
      cursor: pointer;
    }

    .profile-avatar {
      text-align: center;
      margin-bottom: 20px;
      color: #666;
    }

    .profile-info {
      display: flex;
      flex-direction: column;
      gap: 15px;
    }

    .info-item {
      display: flex;
      align-items: center;
      gap: 10px;
      i {
        width: 20px;
        color: #666;
      }
    }

    .profile-edit {
      .form-group {
        margin-bottom: 15px;
        label {
          display: flex;
          align-items: center;
          gap: 10px;
          margin-bottom: 5px;
        }
      }
    }

    .button-group {
      display: flex;
      gap: 10px;
      justify-content: flex-end;
      margin-top: 20px;
    }

    .edit-btn, .save-btn, .cancel-btn {
      padding: 8px 16px;
      border-radius: 4px;
      border: none;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 5px;
    }

    .edit-btn {
      background-color: #4CAF50;
      color: white;
    }

    .save-btn {
      background-color: #2196F3;
      color: white;
    }

    .cancel-btn {
      background-color: #f44336;
      color: white;
    }
      /* main-app.component.scss */
.main-container {
    display: flex;
    height: 97.7vh;
    background-color: #F4F3EE;
  }
  
  .sidebar {
    width: 18%;
    background-color: #463F3A;
    color: white;
    padding: 2rem;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    margin-right: 5rem;
  }
  
  .app-title {
    display: flex;
    align-items: center;
    gap: 1rem;
    color: #F4F3EE;
    margin-top: -1rem;

  }
  
  .sidebar-buttons {
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }
  
  .main-content {
    width: 70%;
    padding: 1rem;
    background-color: #F4F3EE;
    margin-top: -1rem;
    margin-left: -2rem;

  }
  
  .solver-form {
    background-color: white;
    padding: 1.5rem;
    border-radius: 8px;
    margin-top: 2rem;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    margin-top: -1rem;
    margin-bottom: 0.4rem;

  }
  
  .form-group {
    margin-bottom: 0.5rem;
  
    label {
      display: block;
      margin-bottom: 0.5rem;
      color: #463F3A;
    }
  }
  
  .form-control {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #F4F3EE;
    border-radius: 4px;
  }
  
  .solve-btn {
    background-color: #463F3A;
    color: white;
    padding: 0.75rem 1.5rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.2s;
  
    &:hover {
      background-color: darken(#F4F3EE, 10%);
    }
  
    i {
      margin-right: 0.5rem;
    }
  }
  
  .chart-container {
    margin-top: -2rem;
    height: 300px;
    margin: 1rem 0;
    background-color: white;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  
  .table-container {
    margin: 2rem 0;
    overflow-x: auto;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  
  table {
    width: 100%;
    border-collapse: collapse;
  
    th, td {
      padding: 0.75rem;
      border: 1px solid #BCB8B1;
      text-align: right;
    }
  
    th {
      background-color: #8A817C;
      color: white;
    }
  }
  
  .download-buttons {
    display: flex;
    gap: 1rem;
  }
  
  .download-btn {
    background-color: #8A817C;
    color: #F4F3EE;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 0.5rem;
    transition: background-color 0.2s;
  
    &:hover {
      background-color: darken(#463F3A, 10%);
    }
  
    i {
      margin-right: 0.5rem;
    }
  }
  
  .profile-btn, .logout-btn {
    width: 100%;
    padding: 0.75rem;
    background-color: transparent;
    border: 1px solid #8A817C;
    color: #F4F3EE;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 0.5rem;
    transition: all 0.2s;
  
    &:hover {
      background-color: #8A817C;
      color: #463F3A;
    }
  }

  /* Add these styles to your existing SCSS file */

.chart-wrapper {
    width: 100%;
    padding: 1rem;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin: 2rem 0;
  }
  
  .chart-container {
    width: 100%;
    height: 400px;
    margin: 0 auto;
  }
  
  /* Make sure the container has a background and proper sizing */
  .results-section {
    background: #F4F3EE;
    padding: 2rem;
    border-radius: 8px;
    margin-top: 2rem;
  }

  .chart-container {
    width: 100%;
    height: 400px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin: 2rem 0;
    padding: 1rem;
  }
  .chart-container {
    width: 100%;
    height: 300px;
    margin-top: -2rem;

    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    margin: 1rem 0;
    padding: 0.5rem;
    margin-top: 0.5rem;

  }



  /* Modal Styles */
.modal {
    position: fixed; /* Fixed position on screen */
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.5); /* Semi-transparent background */
    z-index: 1000; /* Make sure the modal appears on top */
  }
    .edit-btn {
  background-color: #463F3A;
  color: white;
}

  
 
  `]
})
export class MainAppComponent implements AfterViewInit, OnDestroy {
  @ViewChild('chartDiv') chartDiv!: ElementRef;

  odeType: string = 'first_order';
  xStart: number = 1;
  xEnd: number = 2;
  initialDy: number = 0; // For second order ODEs
  solution: number[][] | null = null;
  private chart: echarts.ECharts | null = null;

 // Updated profile properties
 showProfileModal: boolean = false;
 isEditing: boolean = false;
 profileData: ProfileData = {
   idAccount: 0,
   firstName: '',
   lastName: '',
   email: '',
   gender: '',
   birthdate: '',
   password: null
 };

  constructor(private http: HttpClient, private router: Router) {
    this.fetchProfileData();

  }

  ngAfterViewInit() {
    if (this.chartDiv) {
      this.chart = echarts.init(this.chartDiv.nativeElement);
      this.setEmptyChart();
    }
  }

  ngOnDestroy() {
    if (this.chart) {
      this.chart.dispose();
    }
  }
 
  solve() {
    const payload = {
      ode_type: this.odeType,
      x_start: this.xStart,
      x_end: this.xEnd,
    };

    this.http.post<ODEResponse>('http://localhost:5000/solve-ode', payload)
      .subscribe({
        next: (response) => {
          this.solution = response.solution;
          this.updateChart();
        },
        error: (error) => {
          console.error('Error solving ODE:', error);
        }
      });
  }

  private setEmptyChart() {
    if (!this.chart) return;

    const option = {
      grid: {
        left: '5%',
        right: '5%',
        bottom: '10%',
        top: '10%'
      },
      title: {
        text: 'ODE Solution',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'value',
        name: 'x'
      },
      yAxis: {
        type: 'value',
        name: 'y'
      },
      series: [{
        data: [],
        type: 'line',
        smooth: true
      }]
    };

    this.chart.setOption(option);
  }

  private updateChart() {
    if (!this.chart || !this.solution) return;

    const xValues = this.solution.map((_, i) => 
      this.xStart + (i * ((this.xEnd - this.xStart) / (this.solution!.length - 1)))
    );

    const option = {
      series: [{
        data: this.solution.map((point, i) => [xValues[i], point[0]]),
        type: 'line',
        smooth: true,
        lineStyle: {
          color: '#E0AFA0',
          width: 2
        },
        symbol: 'circle',
        symbolSize: 6
      }]
    };

    this.chart.setOption(option);
  }

  // Profile related methods
  openProfileModal() {
    this.showProfileModal = true;
  }

  closeProfileModal() {
    this.showProfileModal = false;
    this.isEditing = false;
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }



  downloadPDF(sendByEmail: boolean = false) {
    if (!this.solution || !this.chart) return;
  
    const pdf = new jsPDF();
    pdf.text('ODE Solution Results', 20, 20);
  
    // Get the chart data URL
    const dataURL = this.chart.getDataURL();
    const img = new Image();
    img.src = dataURL;
  
    img.onload = () => {
      try {
        const canvas = document.createElement('canvas');
        const context = canvas.getContext('2d');
        if (!context) {
          console.error('Error: Could not get canvas context');
          return;
        }
  
        canvas.width = img.width * 0.5; // Scale image to 50% to reduce size
        canvas.height = img.height * 0.5;
        context.drawImage(img, 0, 0, canvas.width, canvas.height);
  
        const compressedDataURL = canvas.toDataURL('image/png', 0.5); // Adjust compression ratio (0.5)
        pdf.addImage(compressedDataURL, 'PNG', 20, 40, 170, 100);
  
        const startY = 160;
        pdf.text('Solution Data:', 20, startY);
  
        // Add this check to ensure that 'this.solution' is not null or undefined
        if (this.solution) {
          this.solution.forEach((point, i) => {
            const x =
              this.xStart +
              (i * ((this.xEnd - this.xStart) / (this.solution!.length - 1)));
            pdf.text(`x: ${x.toFixed(4)}, y: ${point[0].toFixed(4)}`, 20, startY + 10 + i * 7);
          });
        } else {
          console.error('Solution data is not available.');
        }
  
        // Convert PDF to base64 (remove "data:" prefix)
        const pdfBase64 = pdf.output('datauristring').split(',')[1];
  
        const fileName = 'ode-solution.pdf';
  
        if (sendByEmail) {
          // Send base64 PDF by email
          this.sendPDFByEmail(pdfBase64);
        } else {
          // Save PDF locally
          pdf.save(fileName);
        }
      } catch (error) {
        console.error('Error generating PDF:', error);
      }
    };
  
    img.onerror = (err) => {
      console.error('Error loading image:', err);
    };
  }
  



  sendPDFByEmail(pdfBase64: string) {
    const email = localStorage.getItem('mail');
    
    // Create the payload
    const payload = {
      email: email,
      pdfBase64: pdfBase64
    };
  
    const apiUrl = 'http://77.37.86.136:8002/email/send-pdf'; // Replace with your API endpoint
  
    // Make the POST request
    this.http.post(apiUrl, payload).subscribe(
      (response) => {
        console.log('Email sent successfully:', response);
        alert('File sent to email successfully!');
      },
      (error) => {
        console.error('Error sending email:', error);
        alert('Failed to send email. Please check your API and try again.');
      }
    );
  }
  


  
// Existing download methods remain the same
downloadedPDF() {
  if (!this.solution || !this.chart) return;

  const pdf = new jsPDF();
  pdf.text('ODE Solution Results', 20, 20);

  const dataURL = this.chart.getDataURL();
  pdf.addImage(dataURL, 'PNG', 20, 40, 170, 100);

  const startY = 160;
  pdf.text('Solution Data:', 20, startY);

  this.solution.forEach((point, i) => {
    const x = this.xStart + (i * ((this.xEnd - this.xStart) / (this.solution!.length - 1)));
    pdf.text(`x: ${x.toFixed(4)}, y: ${point[0].toFixed(4)}`, 20, startY + 10 + (i * 7));
  });

  pdf.save('ode-solution.pdf');
}

  downloadCSV() {
    if (!this.solution) return;

    let csv = 'x,y\n';
    
    this.solution.forEach((point, i) => {
      const x = this.xStart + (i * ((this.xEnd - this.xStart) / (this.solution!.length - 1)));
      csv += `${x.toFixed(4)},${point[0].toFixed(4)}\n`;
    });

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'ode-solution.csv');
  }
  saveProfile() {
    const token = localStorage.getItem('authToken');
    const password1 = localStorage.getItem('mdp');
  
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`).set('Content-Type', 'application/json');
  
      // Construct the profile data JSON object as per the required format
      const updatedProfileData = {
        idAccount: this.profileData.idAccount,
        firstName: this.profileData.firstName,
        lastName: this.profileData.lastName,
        email: this.profileData.email,
        gender: this.profileData.gender,
        birthdate: this.profileData.birthdate,
        password: password1 // Ensure that password is included if required by the API
      };
  console.log(this.profileData.password);
      this.http.put('http://77.37.86.136:8002/account/individual/default-method/update', updatedProfileData, { headers })
        .subscribe({
          next: (response) => {
            console.log('Profile updated successfully', response);
            this.isEditing = false; // Set isEditing to false after success

          },
          error: (error) => {
            console.error('Error updating profile:', error);
          }
        });
    } else {
      console.error('No authentication token found.');
    }
  }
  
  
  
  fetchProfileData() {
    const token = localStorage.getItem('authToken');
    if (token) {
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      this.http.get<ProfileData>('http://77.37.86.136:8002/account/individual/default-method/read', { headers })
        .subscribe({
          next: (data) => {
            this.profileData = data;
          },
          error: (error) => {
            console.error('Error fetching profile data:', error);
          }
        });
    }
 
  }
  logout() {
    // Clear local storage
    localStorage.clear();

    // Redirect to the sign-in page
    this.router.navigate(['/signin']);

  }
}