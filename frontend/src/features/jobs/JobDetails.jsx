import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Paper,
  Typography,
  Grid,
  Button,
  Chip,
  Divider,
  CircularProgress,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
} from '@mui/material';
import {
  Edit as EditIcon,
  Delete as DeleteIcon,
  ArrowBack as ArrowBackIcon,
} from '@mui/icons-material';
import axios from 'axios';
import { useAuth } from '../../hooks/useAuth';

const statusColors = {
  Applied: 'primary',
  Interview: 'info',
  Offer: 'success',
  Rejected: 'error',
  'Not Applied': 'default',
};

function JobDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();
  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);

  useEffect(() => {
    fetchJobDetails();
  }, [id, token]);

  const fetchJobDetails = async () => {
    try {
      const response = await axios.get(`/api/jobs/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setJob(response.data);
    } catch (err) {
      setError('Failed to load job details');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`/api/jobs/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      navigate('/jobs');
    } catch (err) {
      setError('Failed to delete job');
      console.error(err);
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box sx={{ mt: 4 }}>
        <Alert severity="error">{error}</Alert>
      </Box>
    );
  }

  if (!job) {
    return (
      <Box sx={{ mt: 4 }}>
        <Alert severity="error">Job not found</Alert>
      </Box>
    );
  }

  return (
    <Box>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/jobs')}
        >
          Back to Jobs
        </Button>
        <Box>
          <Button
            variant="outlined"
            startIcon={<EditIcon />}
            onClick={() => navigate(`/jobs/${id}/edit`)}
            sx={{ mr: 1 }}
          >
            Edit
          </Button>
          <Button
            variant="outlined"
            color="error"
            startIcon={<DeleteIcon />}
            onClick={() => setDeleteDialogOpen(true)}
          >
            Delete
          </Button>
        </Box>
      </Box>

      <Paper sx={{ p: 3 }}>
        <Box sx={{ mb: 3 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            {job.position}
          </Typography>
          <Typography variant="h6" color="text.secondary" gutterBottom>
            {job.company}
          </Typography>
          <Chip
            label={job.status}
            color={statusColors[job.status]}
            sx={{ mr: 1 }}
          />
          <Chip label={job.jobType} sx={{ mr: 1 }} />
          <Chip label={job.field} />
        </Box>

        <Divider sx={{ my: 3 }} />

        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle2" color="text.secondary">
              Date Applied
            </Typography>
            <Typography variant="body1" gutterBottom>
              {new Date(job.dateApplied).toLocaleDateString()}
            </Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle2" color="text.secondary">
              Location
            </Typography>
            <Typography variant="body1" gutterBottom>
              {job.location || 'Not specified'}
            </Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle2" color="text.secondary">
              Salary Range
            </Typography>
            <Typography variant="body1" gutterBottom>
              {job.salaryRange || 'Not specified'}
            </Typography>
          </Grid>
          <Grid item xs={12} sm={6}>
            <Typography variant="subtitle2" color="text.secondary">
              Priority
            </Typography>
            <Typography variant="body1" gutterBottom>
              {job.priority}
            </Typography>
          </Grid>
          {job.nextInterviewDate && (
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle2" color="text.secondary">
                Next Interview
              </Typography>
              <Typography variant="body1" gutterBottom>
                {new Date(job.nextInterviewDate).toLocaleString()}
              </Typography>
            </Grid>
          )}
          {job.offerSalary && (
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle2" color="text.secondary">
                Offer Salary
              </Typography>
              <Typography variant="body1" gutterBottom>
                {job.offerSalary}
              </Typography>
            </Grid>
          )}
          {job.joiningDate && (
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle2" color="text.secondary">
                Joining Date
              </Typography>
              <Typography variant="body1" gutterBottom>
                {new Date(job.joiningDate).toLocaleDateString()}
              </Typography>
            </Grid>
          )}
          {job.rejectionReason && (
            <Grid item xs={12} sm={6}>
              <Typography variant="subtitle2" color="text.secondary">
                Rejection Reason
              </Typography>
              <Typography variant="body1" gutterBottom>
                {job.rejectionReason}
              </Typography>
            </Grid>
          )}
        </Grid>

        <Divider sx={{ my: 3 }} />

        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Typography variant="subtitle2" color="text.secondary">
              Notes
            </Typography>
            <Typography variant="body1" sx={{ whiteSpace: 'pre-wrap' }}>
              {job.notes || 'No notes'}
            </Typography>
          </Grid>
          {job.jobPostUrl && (
            <Grid item xs={12}>
              <Typography variant="subtitle2" color="text.secondary">
                Job Post URL
              </Typography>
              <Typography variant="body1">
                <a href={job.jobPostUrl} target="_blank" rel="noopener noreferrer">
                  {job.jobPostUrl}
                </a>
              </Typography>
            </Grid>
          )}
          {job.applicationLink && (
            <Grid item xs={12}>
              <Typography variant="subtitle2" color="text.secondary">
                Application Link
              </Typography>
              <Typography variant="body1">
                <a
                  href={job.applicationLink}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  {job.applicationLink}
                </a>
              </Typography>
            </Grid>
          )}
        </Grid>
      </Paper>

      <Dialog
        open={deleteDialogOpen}
        onClose={() => setDeleteDialogOpen(false)}
      >
        <DialogTitle>Delete Job</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete this job application? This action
            cannot be undone.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleDelete} color="error">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default JobDetails; 