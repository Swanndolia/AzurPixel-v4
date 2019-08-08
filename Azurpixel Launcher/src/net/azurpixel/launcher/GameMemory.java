package net.azurpixel.launcher;

import java.util.Arrays;
import java.util.List;

public enum GameMemory {

	XMX1G("1 Go", "-Xmx1G", "-Xmx1G", "-Xmn256M", "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-XX:+UseNUMA", "-XX:+CMSParallelRemarkEnabled", "-XX:MaxTenuringThreshold=15", "-XX:MaxGCPauseMillis=30", "-XX:GCPauseIntervalMillis=150", "-XX:+UseAdaptiveGCBoundary", "-XX:-UseGCOverheadLimit", "-XX:+UseBiasedLocking", "-XX:SurvivorRatio=8", "-XX:TargetSurvivorRatio=90", "-XX:MaxTenuringThreshold=15", "-Dfml.ignorePatchDiscrepancies=true", "-Dfml.ignoreInvalidMinecraftCertificates=true", "-XX:+UseFastAccessorMethods", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:+AggressiveOpts", "-XX:ReservedCodeCacheSize=2048m", "-XX:+UseCodeCacheFlushing", "-XX:SoftRefLRUPolicyMSPerMB=3750", "-XX:ParallelGCThreads=10"),
	XMX2G("2 Go", "-Xmx2G", "-Xms2G", "-Xmn512M", "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-XX:+UseNUMA", "-XX:+CMSParallelRemarkEnabled", "-XX:MaxTenuringThreshold=15", "-XX:MaxGCPauseMillis=30", "-XX:GCPauseIntervalMillis=150", "-XX:+UseAdaptiveGCBoundary", "-XX:-UseGCOverheadLimit", "-XX:+UseBiasedLocking", "-XX:SurvivorRatio=8", "-XX:TargetSurvivorRatio=90", "-XX:MaxTenuringThreshold=15", "-Dfml.ignorePatchDiscrepancies=true", "-Dfml.ignoreInvalidMinecraftCertificates=true", "-XX:+UseFastAccessorMethods", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:+AggressiveOpts", "-XX:ReservedCodeCacheSize=2048m", "-XX:+UseCodeCacheFlushing", "-XX:SoftRefLRUPolicyMSPerMB=7500", "-XX:ParallelGCThreads=10"),
	XMX4G("4 Go", "-Xmx4G", "-Xms4G", "-Xmn1G", "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-XX:+UseNUMA", "-XX:+CMSParallelRemarkEnabled", "-XX:MaxTenuringThreshold=15", "-XX:MaxGCPauseMillis=30", "-XX:GCPauseIntervalMillis=150", "-XX:+UseAdaptiveGCBoundary", "-XX:-UseGCOverheadLimit", "-XX:+UseBiasedLocking", "-XX:SurvivorRatio=8", "-XX:TargetSurvivorRatio=90", "-XX:MaxTenuringThreshold=15", "-Dfml.ignorePatchDiscrepancies=true", "-Dfml.ignoreInvalidMinecraftCertificates=true", "-XX:+UseFastAccessorMethods", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:+AggressiveOpts", "-XX:ReservedCodeCacheSize=2048m", "-XX:+UseCodeCacheFlushing", "-XX:SoftRefLRUPolicyMSPerMB=15000", "-XX:ParallelGCThreads=10"),
	XMX6G("6 Go", "-Xmx6G", "-Xms6G", "-Xmn1536M", "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-XX:+UseNUMA", "-XX:+CMSParallelRemarkEnabled", "-XX:MaxTenuringThreshold=15", "-XX:MaxGCPauseMillis=30", "-XX:GCPauseIntervalMillis=150", "-XX:+UseAdaptiveGCBoundary", "-XX:-UseGCOverheadLimit", "-XX:+UseBiasedLocking", "-XX:SurvivorRatio=8", "-XX:TargetSurvivorRatio=90", "-XX:MaxTenuringThreshold=15", "-Dfml.ignorePatchDiscrepancies=true", "-Dfml.ignoreInvalidMinecraftCertificates=true", "-XX:+UseFastAccessorMethods", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:+AggressiveOpts", "-XX:ReservedCodeCacheSize=2048m", "-XX:+UseCodeCacheFlushing", "-XX:SoftRefLRUPolicyMSPerMB=22500", "-XX:ParallelGCThreads=10"),
	XMX8G("8 Go", "-Xmx8G", "-Xms8G", "-Xmn2G", "-XX:+DisableExplicitGC", "-XX:+UseConcMarkSweepGC", "-XX:+UseParNewGC", "-XX:+UseNUMA", "-XX:+CMSParallelRemarkEnabled", "-XX:MaxTenuringThreshold=15", "-XX:MaxGCPauseMillis=30", "-XX:GCPauseIntervalMillis=150", "-XX:+UseAdaptiveGCBoundary", "-XX:-UseGCOverheadLimit", "-XX:+UseBiasedLocking", "-XX:SurvivorRatio=8", "-XX:TargetSurvivorRatio=90", "-XX:MaxTenuringThreshold=15", "-Dfml.ignorePatchDiscrepancies=true", "-Dfml.ignoreInvalidMinecraftCertificates=true", "-XX:+UseFastAccessorMethods", "-XX:+UseCompressedOops", "-XX:+OptimizeStringConcat", "-XX:+AggressiveOpts", "-XX:ReservedCodeCacheSize=2048m", "-XX:+UseCodeCacheFlushing", "-XX:SoftRefLRUPolicyMSPerMB=30000", "-XX:ParallelGCThreads=10");

	private String name;
	private List<String> vmArgs;

	GameMemory(String name, String... vmArgs) {
		this.name = name;
		this.vmArgs = Arrays.asList(vmArgs);
	}

	@Override
	public String toString() {
		return name;
	}

	public List<String> getVmArgs() {
		return vmArgs;
	}
}
