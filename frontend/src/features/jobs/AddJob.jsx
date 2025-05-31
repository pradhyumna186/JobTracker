import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  MenuItem,
  Alert,
  CircularProgress,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import axios from 'axios';
import { useAuth } from '../../hooks/useAuth';

const jobTypes = ['Full-Time', 'Internship', 'Contract'];
const fields = ['SDE', 'Data Science', 'Product Management', 'Design', 'Other'];
const statuses = ['Not Applied', 'Applied', 'Interview', 'Offer', 'Rejected'];
const priorities = ['High', 'Normal', 'Low'];

function AddJob() {
  const navigate = useNavigate();
  const { token } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState({
    company: '',
    position: '',
    status: 'Not Applied',
    jobType: '',
    field: '',
    dateApplied: new Date(),
    location: '',
    salaryRange: '',
    notes: '',
    jobPostUrl: '',
    applicationSource: '',
    applicationLink: '',
    nextInterviewDate: null,
    priority: 'Normal',
    offerSalary: '',
    joiningDate: null,
    rejectionReason: '',
  });

  const handleChange = (field) => (event) => {
    setFormData((prev) => ({
      ...prev,
      [field]: event.target.value,
    }));
  };

  const handleDateChange = (field) => (date) => {
    setFormData((prev) => ({
      ...prev,
      [field]: date,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await axios.post(
        '/api/jobs',
        {
          ...formData,
          dateApplied: formData.dateApplied.toISOString(),
          nextInterviewDate: formData.nextInterviewDate?.toISOString(),
          joiningDate: formData.joiningDate?.toISOString(),
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      navigate('/jobs');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to add job');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Add New Job
      </Typography>

      <Paper sx={{ p: 3 }}>
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Company"
                value={formData.company}
                onChange={handleChange('company')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Position"
                value={formData.position}
                onChange={handleChange('position')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                select
                label="Status"
                value={formData.status}
                onChange={handleChange('status')}
              >
                {statuses.map((status) => (
                  <MenuItem key={status} value={status}>
                    {status}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                select
                label="Job Type"
                value={formData.jobType}
                onChange={handleChange('jobType')}
              >
                {jobTypes.map((type) => (
                  <MenuItem key={type} value={type}>
                    {type}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                select
                label="Field"
                value={formData.field}
                onChange={handleChange('field')}
              >
                {fields.map((field) => (
                  <MenuItem key={field} value={field}>
                    {field}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6}>
              <DatePicker
                label="Date Applied"
                value={formData.dateApplied}
                onChange={handleDateChange('dateApplied')}
                renderInput={(params) => <TextField {...params} fullWidth />}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Location"
                value={formData.location}
                onChange={handleChange('location')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Salary Range"
                value={formData.salaryRange}
                onChange={handleChange('salaryRange')}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                multiline
                rows={4}
                label="Notes"
                value={formData.notes}
                onChange={handleChange('notes')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Job Post URL"
                value={formData.jobPostUrl}
                onChange={handleChange('jobPostUrl')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Application Source"
                value={formData.applicationSource}
                onChange={handleChange('applicationSource')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Application Link"
                value={formData.applicationLink}
                onChange={handleChange('applicationLink')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <DatePicker
                label="Next Interview Date"
                value={formData.nextInterviewDate}
                onChange={handleDateChange('nextInterviewDate')}
                renderInput={(params) => <TextField {...params} fullWidth />}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                select
                label="Priority"
                value={formData.priority}
                onChange={handleChange('priority')}
              >
                {priorities.map((priority) => (
                  <MenuItem key={priority} value={priority}>
                    {priority}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Offer Salary"
                value={formData.offerSalary}
                onChange={handleChange('offerSalary')}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <DatePicker
                label="Joining Date"
                value={formData.joiningDate}
                onChange={handleDateChange('joiningDate')}
                renderInput={(params) => <TextField {...params} fullWidth />}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Rejection Reason"
                value={formData.rejectionReason}
                onChange={handleChange('rejectionReason')}
              />
            </Grid>
          </Grid>

          <Box sx={{ mt: 3, display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
            <Button
              variant="outlined"
              onClick={() => navigate('/jobs')}
              disabled={loading}
            >
              Cancel
            </Button>
            <Button
              type="submit"
              variant="contained"
              disabled={loading}
              startIcon={loading && <CircularProgress size={20} />}
            >
              {loading ? 'Adding...' : 'Add Job'}
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
}

export default AddJob; 