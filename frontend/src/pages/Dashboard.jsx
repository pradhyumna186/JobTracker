import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const [jobs, setJobs] = useState([]);
  const [sortOption, setSortOption] = useState('newest'); // Sorting option
  const [company, setCompany] = useState('');
  const [position, setPosition] = useState('');
  const [status, setStatus] = useState('Applied');
  const [jobType, setJobType] = useState('');
  const [field, setField] = useState('');
  const [role, setRole] = useState('');
  const [progress, setProgress] = useState(0);
  const [dateApplied, setDateApplied] = useState('');
  const [location, setLocation] = useState('');
  const [salaryRange, setSalaryRange] = useState('');
  const [notes, setNotes] = useState('');
  const [jobPostUrl, setJobPostUrl] = useState('');
  const [editingJobId, setEditingJobId] = useState(null);
  const [editCompany, setEditCompany] = useState('');
  const [editPosition, setEditPosition] = useState('');
  const [editStatus, setEditStatus] = useState('Applied');
  const [addingRoundJobId, setAddingRoundJobId] = useState(null);
  const [roundName, setRoundName] = useState('');
  const [roundStatus, setRoundStatus] = useState('Scheduled');
  const [roundNotes, setRoundNotes] = useState('');
  const [roundScheduledDateTime, setRoundScheduledDateTime] = useState('');

  const navigate = useNavigate();

  const fetchJobs = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        alert('Please login first!');
        navigate('/login');
        return;
      }
      const response = await axios.get('http://localhost:8080/api/jobs', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setJobs(response.data);
    } catch (error) {
      console.error(error);
      alert('Failed to fetch jobs. Please login again.');
      navigate('/login');
    }
  };

  useEffect(() => {
    fetchJobs();
  }, [navigate]);

  const sortJobs = (jobs) => {
    switch (sortOption) {
      case 'newest':
        return [...jobs].sort((a, b) => new Date(b.dateApplied) - new Date(a.dateApplied));
      case 'oldest':
        return [...jobs].sort((a, b) => new Date(a.dateApplied) - new Date(b.dateApplied));
      case 'company':
        return [...jobs].sort((a, b) => (a.company || '').localeCompare(b.company || ''));
      case 'progress':
        return [...jobs].sort((a, b) => (b.progress || 0) - (a.progress || 0));
      default:
        return jobs;
    }
  };

  const handleCreateJob = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8080/api/jobs', {
        company,
        position,
        status,
        jobType,
        field,
        role,
        progress,
        dateApplied,
        location,
        salaryRange,
        notes,
        jobPostUrl
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      alert('Job created successfully!');
      setCompany('');
      setPosition('');
      setStatus('Applied');
      setJobType('');
      setField('');
      setRole('');
      setProgress(0);
      setDateApplied('');
      setLocation('');
      setSalaryRange('');
      setNotes('');
      setJobPostUrl('');
      fetchJobs();
    } catch (error) {
      console.error(error);
      alert('Failed to create job.');
    }
  };

  const handleDeleteJob = async (jobId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.delete(`http://localhost:8080/api/jobs/${jobId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      alert('Job deleted successfully!');
      fetchJobs();
    } catch (error) {
      console.error(error);
      alert('Failed to delete job.');
    }
  };

  const startEditing = (job) => {
    setEditingJobId(job.id);
    setEditCompany(job.company);
    setEditPosition(job.position);
    setEditStatus(job.status);
  };

  const handleEditJob = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.put(`http://localhost:8080/api/jobs/${editingJobId}`, {
        company: editCompany,
        position: editPosition,
        status: editStatus
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      alert('Job updated successfully!');
      setEditingJobId(null);
      fetchJobs();
    } catch (error) {
      console.error(error);
      alert('Failed to update job.');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  const handleCreateRound = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.post(`http://localhost:8080/api/jobs/${addingRoundJobId}/rounds`, {
        roundName,
        status: roundStatus,
        notes: roundNotes,
        scheduledDateTime: roundScheduledDateTime
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      alert('Round added successfully!');
      setAddingRoundJobId(null);
      setRoundName('');
      setRoundStatus('Scheduled');
      setRoundNotes('');
      setRoundScheduledDateTime('');
      fetchJobs();
    } catch (error) {
      console.error(error);
      alert('Failed to add round.');
    }
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '30px' }}>
      <button onClick={handleLogout} style={{ position: 'absolute', top: '20px', right: '20px' }}>
        Logout
      </button>

      <h1>Dashboard - My Job Applications</h1>

      {/* Sort Dropdown */}
      <div style={{ marginBottom: '20px' }}>
        <select value={sortOption} onChange={(e) => setSortOption(e.target.value)}>
          <option value="newest">Sort by Newest</option>
          <option value="oldest">Sort by Oldest</option>
          <option value="company">Sort by Company Name</option>
          <option value="progress">Sort by Progress</option>
        </select>
      </div>

      {/* Create Job Form */}
      <div style={{ marginBottom: '40px' }}>
        <h2>Create New Job</h2>
        <form onSubmit={handleCreateJob}>
          {/* Inputs */}
          <input type="text" placeholder="Company" value={company} onChange={(e) => setCompany(e.target.value)} required /><br /><br />
          <input type="text" placeholder="Position" value={position} onChange={(e) => setPosition(e.target.value)} required /><br /><br />
          <input type="text" placeholder="Job Type" value={jobType} onChange={(e) => setJobType(e.target.value)} /><br /><br />
          <input type="text" placeholder="Field" value={field} onChange={(e) => setField(e.target.value)} /><br /><br />
          <input type="text" placeholder="Role" value={role} onChange={(e) => setRole(e.target.value)} /><br /><br />
          <input type="number" placeholder="Progress (0-100)" value={progress} onChange={(e) => setProgress(e.target.value)} /><br /><br />
          <input type="date" value={dateApplied} onChange={(e) => setDateApplied(e.target.value)} /><br /><br />
          <input type="text" placeholder="Location" value={location} onChange={(e) => setLocation(e.target.value)} /><br /><br />
          <input type="text" placeholder="Salary Range" value={salaryRange} onChange={(e) => setSalaryRange(e.target.value)} /><br /><br />
          <input type="text" placeholder="Notes" value={notes} onChange={(e) => setNotes(e.target.value)} /><br /><br />
          <input type="text" placeholder="Job Post URL" value={jobPostUrl} onChange={(e) => setJobPostUrl(e.target.value)} /><br /><br />
          <select value={status} onChange={(e) => setStatus(e.target.value)}>
            <option value="Applied">Applied</option>
            <option value="Interview">Interview</option>
            <option value="Offer">Offer</option>
            <option value="Rejected">Rejected</option>
          </select><br /><br />
          <button type="submit">Create Job</button>
        </form>
      </div>

      {/* Jobs List */}
      {sortJobs(jobs).length === 0 ? (
        <p>No jobs found.</p>
      ) : (
        <ul style={{ listStyleType: 'none', padding: 0 }}>
          {sortJobs(jobs).map((job) => (
            <li key={job.id} style={{ marginBottom: '30px', border: '1px solid black', padding: '10px' }}>
              <h3>{job.company} - {job.position}</h3>
              <p>Status: {job.status}</p>
              <p>Progress: {job.progress}%</p>
              <p>Date Applied: {job.dateApplied}</p>
              <p>Location: {job.location}</p>
              <p>Salary: {job.salaryRange}</p>
              <p>Notes: {job.notes}</p>
              <p><a href={job.jobPostUrl} target="_blank" rel="noopener noreferrer">Job Link</a></p>

              {/* Interview Rounds */}
              <div>
                <h4>Interview Rounds:</h4>
                {job.rounds && job.rounds.length > 0 ? (
                  <ul>
                    {job.rounds.map((round, index) => (
                      <li key={index}>
                        {round.roundName} - {round.status}
                        {round.scheduledDateTime && (
                          <div>Scheduled: {new Date(round.scheduledDateTime).toLocaleString()}</div>
                        )}
                        {round.notes && <div>Notes: {round.notes}</div>}
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p>No rounds added yet.</p>
                )}
              </div>

              {/* Add/Edit/Delete Buttons */}
              <button onClick={() => setAddingRoundJobId(job.id)} style={{ marginTop: '10px' }}>
                Add New Round
              </button>
              <br /><br />
              <button onClick={() => startEditing(job)}>Edit Job</button>
              <button onClick={() => handleDeleteJob(job.id)} style={{ marginLeft: '10px', color: 'red' }}>Delete Job</button>

              {/* Add Round Form */}
              {addingRoundJobId === job.id && (
                <form onSubmit={handleCreateRound} style={{ marginTop: '10px' }}>
                  <input type="text" placeholder="Round Name" value={roundName} onChange={(e) => setRoundName(e.target.value)} required />
                  <input type="datetime-local" value={roundScheduledDateTime} onChange={(e) => setRoundScheduledDateTime(e.target.value)} />
                  <select value={roundStatus} onChange={(e) => setRoundStatus(e.target.value)}>
                    <option value="Scheduled">Scheduled</option>
                    <option value="Completed">Completed</option>
                    <option value="Cancelled">Cancelled</option>
                  </select>
                  <input type="text" placeholder="Notes" value={roundNotes} onChange={(e) => setRoundNotes(e.target.value)} />
                  <button type="submit">Save Round</button>
                </form>
              )}
            </li>
          ))}
        </ul>
      )}

      {/* Edit Job Form */}
      {editingJobId && (
        <div style={{ marginTop: '40px' }}>
          <h2>Edit Job</h2>
          <form onSubmit={handleEditJob}>
            <input type="text" placeholder="Company" value={editCompany} onChange={(e) => setEditCompany(e.target.value)} required />
            <input type="text" placeholder="Position" value={editPosition} onChange={(e) => setEditPosition(e.target.value)} required />
            <select value={editStatus} onChange={(e) => setEditStatus(e.target.value)}>
              <option value="Applied">Applied</option>
              <option value="Interview">Interview</option>
              <option value="Offer">Offer</option>
              <option value="Rejected">Rejected</option>
            </select>
            <button type="submit" style={{ marginTop: '10px' }}>Save Changes</button>
          </form>
        </div>
      )}
    </div>
  );
}

export default Dashboard;
