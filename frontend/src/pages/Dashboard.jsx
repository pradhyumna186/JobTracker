import React from 'react';
import { Box, Typography, Paper } from '@mui/material';

function Dashboard() {
  return (
    <Box sx={{ p: 3 }}>
      <Paper
        elevation={3}
        sx={{
          p: 4,
          backgroundColor: 'rgba(255, 255, 255, 0.9)',
          backdropFilter: 'blur(10px)',
          borderRadius: 2,
        }}
      >
        <Typography variant="h4" component="h1" gutterBottom>
          Welcome to Job Tracker
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Start managing your job applications and track your progress.
        </Typography>
      </Paper>
    </Box>
  );
}

export default Dashboard;
