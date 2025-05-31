import { useState, useEffect } from 'react';
import {
  Grid,
  Paper,
  Typography,
  Box,
  CircularProgress,
  Card,
  CardContent,
  List,
  ListItem,
  ListItemText,
  Divider,
} from '@mui/material';
import {
  Work as WorkIcon,
  CheckCircle as CheckCircleIcon,
  Schedule as ScheduleIcon,
  Cancel as CancelIcon,
} from '@mui/icons-material';
import axios from 'axios';
import { useAuth } from '../../hooks/useAuth';

function StatCard({ title, value, icon, color }) {
  return (
    <Card>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          {icon}
          <Typography variant="h6" component="div" sx={{ ml: 1 }}>
            {title}
          </Typography>
        </Box>
        <Typography variant="h4" component="div" sx={{ color }}>
          {value}
        </Typography>
      </CardContent>
    </Card>
  );
}

function Dashboard() {
  const { token } = useAuth();
  const [stats, setStats] = useState({
    total: 0,
    active: 0,
    interviews: 0,
    offers: 0,
  });
  const [recentJobs, setRecentJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [statsResponse, jobsResponse] = await Promise.all([
          axios.get('/api/jobs/stats', {
            headers: { Authorization: `Bearer ${token}` },
          }),
          axios.get('/api/jobs/recent', {
            headers: { Authorization: `Bearer ${token}` },
          }),
        ]);

        setStats(statsResponse.data);
        setRecentJobs(jobsResponse.data);
      } catch (err) {
        setError('Failed to load dashboard data');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [token]);

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
        <Typography color="error">{error}</Typography>
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>

      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Total Applications"
            value={stats.total}
            icon={<WorkIcon color="primary" />}
            color="primary.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Active Applications"
            value={stats.active}
            icon={<ScheduleIcon color="info" />}
            color="info.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Interviews"
            value={stats.interviews}
            icon={<CheckCircleIcon color="success" />}
            color="success.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Offers"
            value={stats.offers}
            icon={<CancelIcon color="error" />}
            color="error.main"
          />
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Recent Applications
            </Typography>
            <List>
              {recentJobs.map((job, index) => (
                <Box key={job.id}>
                  <ListItem>
                    <ListItemText
                      primary={job.position}
                      secondary={`${job.company} • ${job.status}`}
                    />
                  </ListItem>
                  {index < recentJobs.length - 1 && <Divider />}
                </Box>
              ))}
            </List>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Upcoming Interviews
            </Typography>
            <List>
              {recentJobs
                .filter((job) => job.nextInterviewDate)
                .map((job, index) => (
                  <Box key={job.id}>
                    <ListItem>
                      <ListItemText
                        primary={job.position}
                        secondary={`${job.company} • ${new Date(
                          job.nextInterviewDate
                        ).toLocaleDateString()}`}
                      />
                    </ListItem>
                    {index < recentJobs.length - 1 && <Divider />}
                  </Box>
                ))}
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}

export default Dashboard; 