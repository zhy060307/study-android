package alvin.base.service.remote.aidls;

import alvin.base.service.remote.aidls.models.JobResponse;

// This interface use to callback to callers.
interface IOnJobStatusChangeListener {

    // Notifify callers when job is started
    void onJobStart(String name);

    // Notifify callers when job is finished
    // 'in' means this argument should pass to service
    void onJobFinish(in JobResponse response);
}
